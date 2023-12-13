package projectCoffee.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectCoffee.entity.Answer;
import projectCoffee.entity.Member;
import projectCoffee.entity.Question;
import projectCoffee.exception.DateNotFoundException;
import projectCoffee.repository.QuestionRepository;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Transactional
@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    @Transactional(readOnly = true)
    public List<Question> getList() {
        return this.questionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Question getQuestion(Integer id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if(question.isPresent()) {
            return question.get();
        } else {
            throw new DateNotFoundException("question not found");
        }
    }

    public void create(String subject, String content, Member member) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setMember(member);
        this.questionRepository.save(q);
    }
    @Transactional(readOnly = true)
    public Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Question> spec = search(kw);
        return this.questionRepository.findAll(spec, pageable);
    }
    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }
    public void delete(Question question) {
        this.questionRepository.delete(question);
    }

    public void vote(Question question, Member member) {
        question.getVoter().add(member);
        this.questionRepository.save(question);
    }

    private Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true); // 중 복 을 제 거
                Join<Question, Member> u1 = q.join("member", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Answer, Member> u2 = a.join("member", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제 목
                        cb.like(q.get("content"), "%" + kw + "%"), // 내 용
                        cb.like(u1.get("email"), "%" + kw + "%"), // 질 문 작 성 자
                        cb.like(a.get("content"), "%" + kw + "%"), // 답 변 내 용
                        cb.like(u2.get("email"), "%" + kw + "%")); // 답 변 작 성 자
            }
        };
    }
}
