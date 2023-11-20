package fenner.tasks.service;

import fenner.tasks.model.Task;
import fenner.tasks.repository.TaskRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService implements ITaskService {

    @Autowired
    private TaskRespository taskRespository;
    @Override
    public List<Task> listTasks() {
        return taskRespository.findAll();
    }

    @Override
    public Task findById(Integer id) {
        return taskRespository.findById(id).orElse(null);
    }

    @Override
    public void store(Task task) {
        taskRespository.save(task);
    }

    @Override
    public void delete(Task task) {
        taskRespository.delete(task);
    }
}
