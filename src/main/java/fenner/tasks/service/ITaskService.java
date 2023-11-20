package fenner.tasks.service;

import fenner.tasks.model.Task;

import java.util.List;

public interface ITaskService {
    public List<Task> listTasks();

    public Task findById(Integer id);

    public void store(Task task);

    public void delete(Task task);
}
