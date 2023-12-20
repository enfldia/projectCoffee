package projectCoffee.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import projectCoffee.dto.MemberUpdateDto;
import projectCoffee.dto.MemberFormDto;
import projectCoffee.dto.SessionMember;
import projectCoffee.entity.Member;
import projectCoffee.repository.MemberRepository;
import projectCoffee.service.MemberService;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;


@RequestMapping(value = "/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    // 로그인
    @GetMapping(value = "/login")
    public String loginMember() {
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "/member/memberLoginForm";
    }

    // 회원 가입
    @GetMapping(value = "/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/join")
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "member/memberForm";
        }
        // 비밀번호 일치 확인
        if (!memberFormDto.getPassword().equals(memberFormDto.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm",
                    "password.mismatch", "비밀번호가 일치하지 않습니다.");
            return "member/memberForm";
        }
        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }
        return "redirect:/";
    }

    // 회원 정보 조회 (GET)
    @GetMapping(value = "/info")
    public String updateMemberForm(Principal principal, Model model) {
        if (principal != null) {
            String user = principal.getName();
            Member memberInfo = memberRepository.findByEmail(user);
            MemberUpdateDto memberUpdateDto = memberService.getMemberDtl(memberInfo.getId());
            model.addAttribute("memberUpdateDto", memberUpdateDto);
        }
        return "member/memberUpdateForm";
    }

    // 회원 정보 수정 (POST)
    @PostMapping(value = "/update")
    public String updateMember(@Valid MemberUpdateDto memberUpdateDto, BindingResult bindingResult, Principal principal, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "회원 정보 수정을 실패했습니다.");
            return "member/memberUpdateForm";
        }
        try {
            if (principal.getName().equals(memberUpdateDto.getEmail())) {
                memberService.updateMember((memberUpdateDto));
                model.addAttribute("successMessage", "회원 정보 수정이 완료되었습니다.");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "회원 정보 수정 중 에러가 발생하였습니다");
            return "member/memberUpdateForm";
        }
        return "redirect:/";
    }

    // 회원 삭제
    @GetMapping("/delete/{memberId}")
    public String userDelete(@PathVariable("memberId") Long memberId,
                             Authentication authentication, HttpServletRequest request,
                             HttpServletResponse response, Principal principal, Model model) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(EntityNotFoundException::new);
        try {
            if (principal.getName().equals(member.getEmail())) {
                memberRepository.delete(member);

                // 회원 탈퇴 후 로그아웃
                new SecurityContextLogoutHandler().logout(request, response, authentication);

                return "redirect:/";
            }
        } catch (Exception e) {
            model.addAttribute("errorDelete", "탈퇴 중 에러가 발생하였습니다");
            return "/member/memberUpdateForm";
        }
        return "redirect:/";
    }

}
