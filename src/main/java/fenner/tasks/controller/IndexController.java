package fenner.tasks.controller;

import fenner.tasks.model.Task;
import fenner.tasks.service.TaskService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class IndexController implements Initializable {
    private static final Logger logger =
            LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private TaskService taskService;

    @FXML
    private TableView<Task> tasksTable;

    @FXML
    private TableColumn<Task, Integer> idColumn;

    @FXML
    private TableColumn<Task, String> taskColumn;

    @FXML
    private TableColumn<Task, String> responsibleColumn;

    @FXML
    private TableColumn<Task, String> statusColumn;

    private final ObservableList<Task> tasksList =
            FXCollections.observableArrayList();

    @FXML
    private TextField taskField;

    @FXML
    private TextField responsibleField;

    @FXML
    private TextField statusField;

    private Integer idInner;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tasksTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        setColumns();
        listTasks();
    }

    private void setColumns(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        taskColumn.setCellValueFactory(new PropertyValueFactory<>("task"));
        responsibleColumn.setCellValueFactory(new PropertyValueFactory<>("responsible"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void listTasks(){
        logger.info("Listing tasks");
        tasksList.clear();
        tasksList.addAll(taskService.listTasks());
        tasksTable.setItems(tasksList);
    }

    public void addTask(){
        if(taskField.getText().isEmpty()){
            showMessage("Validation Error", "Task is required");
            taskField.requestFocus();
            return;
        }
        else{
            var newTask = new Task();
            getFormData(newTask);
            newTask.setId(null);
            taskService.store(newTask);
            showMessage("Info: ", "task added");
            cleanForm();
            listTasks();
        }
    }

    public void loadTaskForm(){
        var task = tasksTable.getSelectionModel().getSelectedItem();
        if(task != null){
            idInner = task.getId();
            taskField.setText(task.getTask());
            responsibleField.setText(task.getResponsible());
            statusField.setText(task.getStatus());
        }
    }

    private void getFormData(Task task){
        if(idInner != null)
            task.setId(idInner);
        task.setTask(taskField.getText());
        task.setResponsible(responsibleField.getText());
        task.setStatus(statusField.getText());
    }

    public void updateTask(){
        if(idInner == null){
            showMessage("Info:", "Should select at least a task");
            return;
        }
        if(taskField.getText().isEmpty()){
            showMessage("Validation Error", "Should set at least a task");
            taskField.requestFocus();
            return;
        }
        var task = new Task();
        getFormData(task);
        taskService.store(task);
        showMessage("Info:", "task updated");
        cleanForm();
        listTasks();
    }

    public void deleteTask(){
        var task = tasksTable.getSelectionModel().getSelectedItem();
        if(task != null){
            logger.info("Task to delete: " + task.toString());
            taskService.delete(task);
            showMessage("Info:", "task deleted:" + task.getId());
            cleanForm();
            listTasks();
        }
        else{
            showMessage("Error", "Should select at least a task");
        }
    }

    public void cleanForm(){
        idInner = null;
        taskField.clear();
        responsibleField.clear();
        statusField.clear();
    }

    private void showMessage(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
