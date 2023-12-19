package projectCoffee.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import projectCoffee.entity.Member;
import projectCoffee.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2MemberService extends DefaultOAuth2UserService {

//    private final BCryptPasswordEncoder encoder;
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

//        https://growth-coder.tistory.com/136

//        String provider = userRequest.getClientRegistration().getClientId();
//        String providerId = oAuth2User.getAttribute("sub");
//        String email = oAuth2User.getAttribute("email");
//        String role = "ROLE_USER"; //일반 유저
//        String password = passwordEncoder.encode(getPassword());
//        Member findMember = memberRepository.findByEmail(email);
//        if (findMember != null) { //찾지 못했다면
//            Member member = Member.builder()
//                    .password(encoder.encode("password"))
//                    .role(role)
//                    .provider(provider)
//                    .providerId(providerId).build();
//            memberRepository.save(member);
//        }
//        return oAuth2User;

        // 회원 정보 출력
        System.out.println("oAuth2User = " + oAuth2User.getAttributes());
        return super.loadUser(userRequest);

    }


}
