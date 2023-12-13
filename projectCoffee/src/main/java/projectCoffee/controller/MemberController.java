package projectCoffee.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import projectCoffee.dto.MemberUpdateDto;
import projectCoffee.dto.MemberFormDto;
import projectCoffee.entity.Member;
import projectCoffee.repository.MemberRepository;
import projectCoffee.service.MemberService;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URLEncoder;
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
    public String loginMember(){
        return "/member/memberLoginForm";
    }

    // 회원 가입
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

        @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "/member/memberLoginForm";
    }


    // 회원 정보 변경 폼 (GET)
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

    // 회원 정보 조회, 변경 (POST)
    @PostMapping(value = "/update")
    public String updateMember(@Valid MemberUpdateDto memberUpdateDto,  Principal principal, Model model) {

        try {
            if (principal.getName().equals(memberUpdateDto.getEmail())) {
                Member updatedMember = memberService.updateMember(memberUpdateDto.getId(), memberUpdateDto);

            if (updatedMember != null) {
                model.addAttribute("successMsg", "회원 정보 수정이 완료되었습니다.");
            } else {
                model.addAttribute("errorMsg", "회원 정보를 찾을 수 없습니다.");
                return "member/memberUpdateForm";
            }
        }
    } catch (Exception e) {
        e.printStackTrace(); // 예외를 로그에 기록합니다.
        model.addAttribute("errorMsg", "오류가 발생했습니다.");
        return "member/memberUpdateForm";
    }

        return "redirect:/"; // 리다이렉트를 통해 홈 페이지로 이동합니다.
}

    // 회원 삭제

}
