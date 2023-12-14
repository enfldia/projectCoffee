package projectCoffee;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import projectCoffee.entity.Question;
import projectCoffee.repository.QuestionRepository;
import projectCoffee.service.QuestionService;

import java.time.LocalDateTime;

@SpringBootTest
class ProjectCoffeeApplicationTests {

	@Autowired
	private QuestionService questionService;
//	@Test
//	void testJpa() {
//		for (int i = 1; i <= 300; i++) {
//			String subject = String.format("테 스 트 데 이 터 입 니 다:[%03d]", i);
//			String content = "내 용 무";
//			this.questionService.create(subject, content);
//		}
//	}
	@Test
	void contextLoads() {
	}

}
