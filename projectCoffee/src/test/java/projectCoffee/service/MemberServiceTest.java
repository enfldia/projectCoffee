package projectCoffee.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import projectCoffee.dto.MemberFormDto;
import projectCoffee.entity.Member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
//@TestPropertySource(locations = "classpath:application-test.properties")
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setUserId("abcd");
        memberFormDto.setPassword("asdf");
        memberFormDto.setName("이름");
        memberFormDto.setEmail("aaa");
        memberFormDto.setAddress("서울시");
        return Member.createMember(memberFormDto, passwordEncoder);

    }


    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest() {
        Member member = createMember();
        Member savedMember = memberService.saveMember(member);

        assertEquals(member.getUserId(), savedMember.getUserId());
        assertEquals(member.getPassword(), savedMember.getPassword());
        assertEquals(member.getName(), savedMember.getName());
        assertEquals(member.getEmail(), savedMember.getEmail());
        assertEquals(member.getAddress(), savedMember.getAddress());

    }

    @Test
    @DisplayName("중복 회원 가입 테스트")
    public void saveDuplicateMemberTEst() {
        Member member1 = createMember();
        Member member2 = createMember();
        memberService.saveMember(member1);

        Throwable e = assertThrows(IllegalStateException.class, () -> {
            memberService.saveMember(member2);});

        assertEquals("이미 가입된 회원입니다.", e.getMessage());
    }
}