package projectCoffee.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import projectCoffee.dto.MemberEditDto;
import projectCoffee.dto.MemberFormDto;
import projectCoffee.entity.Member;
import projectCoffee.repository.MemberRepository;
import projectCoffee.service.MemberService;

import javax.validation.Valid;
import java.security.Principal;


@RequestMapping(value = "/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;




    @GetMapping(value = "/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()) {
            return "member/memberForm";
        } try {
            Member member = Member.createMember(memberFormDto,passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/info")
        public String memberInfo(Principal principal, ModelMap modelMap){
            String loginId = principal.getName();
            Member member = memberRepository.findByEmail(loginId);
            modelMap.addAttribute("member", member);
        return "member/memberInfo";
    }

    @PostMapping(value = "/update")
        public String updateMember(@Valid MemberEditDto memberEditDto, BindingResult bindingResult, Model model) {



        return "member/memberInfo";
    }

    @GetMapping(value = "/login")
    public String loginMember(){
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "/member/memberLoginForm";
    }


}
