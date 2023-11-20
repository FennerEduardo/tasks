package fenner.tasks.repository;

import fenner.tasks.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRespository extends JpaRepository<Task, Integer> {
}
