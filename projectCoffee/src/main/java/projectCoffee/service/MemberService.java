package projectCoffee.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
<<<<<<< Updated upstream
import projectCoffee.dto.MemberUpdateDto;
=======
import projectCoffee.exception.DataNotFoundException;
>>>>>>> Stashed changes
import projectCoffee.entity.Member;
import projectCoffee.repository.MemberRepository;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");

        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new UsernameNotFoundException(email);
        }
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

<<<<<<< Updated upstream
    public MemberUpdateDto getMemberDtl (Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
        // member 정보를 memberUpdateDto로 변환합니다.
        MemberUpdateDto memberUpdateDto = MemberUpdateDto.of(member);
        return memberUpdateDto;
    }

    // 회원 정보 수정
    public Long updateMember (MemberUpdateDto memberUpdateDto) {
        Member member = memberRepository.findByEmail(memberUpdateDto.getEmail());
        member.updateMember(memberUpdateDto);
        return member.getId();
    }
=======
>>>>>>> Stashed changes
}
