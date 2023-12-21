package projectCoffee.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectCoffee.entity.Answer;
import projectCoffee.entity.Member;
import projectCoffee.entity.Question;
import projectCoffee.exception.DateNotFoundException;
import projectCoffee.repository.AnswerRepository;

import java.time.LocalDateTime;
import java.util.Optional;
@Transactional
@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;

    public Answer create(Question question, String content, Member member) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);
        answer.setMember(member);
        this.answerRepository.save(answer);
        return answer;
    }
    @Transactional(readOnly = true)
    public Answer getAnswer(Integer id) {
        Optional<Answer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DateNotFoundException("answer not found");
        }
    }
    public void modify(Answer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    }
    public void delete(Answer answer) {
        this.answerRepository.delete(answer);
    }

    public void vote(Answer answer, Member member) {
        answer.getVoter().add(member);
        this.answerRepository.save(answer);
    }
}
