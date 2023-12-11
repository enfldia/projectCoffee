package projectCoffee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projectCoffee.entity.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
}
