package projectCoffee.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import projectCoffee.dto.AnswerDto;
import projectCoffee.dto.QuestionDto;
import projectCoffee.entity.Answer;
import projectCoffee.entity.Member;
import projectCoffee.entity.Question;
import projectCoffee.repository.MemberRepository;
import projectCoffee.repository.QuestionRepository;
import projectCoffee.service.QuestionService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionService questionService;
    private final MemberRepository memberRepository;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page) {
        Page<Question> paging = this.questionService.getList(page);
        model.addAttribute("paging", paging);
        return "question/list";
    }
    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerDto answerDto) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionDto questionDto) {
        return "question/form";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionDto questionDto, BindingResult bindingResult, Principal principal) {
        if(bindingResult.hasErrors()) {
            return "question/form";
        }
        Member member = this.memberRepository.findByEmail(principal.getName());
        this.questionService.create(questionDto.getSubject(), questionDto.getContent(), member);
        return "redirect:/question/list";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionDto questionDto, @PathVariable("id")
    Integer id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getMember().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수 정 권 한 이 없 습 니 다.");
        }
        questionDto.setSubject(question.getSubject());
        questionDto.setContent(question.getContent());
        return "question/form";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionDto questionDto, BindingResult
            bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getMember().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수 정 권 한 이 없 습 니 다.");
        }
        this.questionService.modify(question, questionDto.getSubject(), questionDto.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getMember().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭 제 권 한 이 없 습 니 다.");
        }
        this.questionService.delete(question);
        return "redirect:/question/list";
    }
}
