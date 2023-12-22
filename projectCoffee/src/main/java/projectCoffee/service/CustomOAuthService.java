package projectCoffee.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import projectCoffee.dto.MemberFormDto;
import projectCoffee.dto.OAuthAttributes;
import projectCoffee.dto.SessionMember;
import projectCoffee.entity.Member;
import projectCoffee.repository.MemberRepository;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CustomOAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final HttpSession httpSession;
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // OAuth 서비스 id
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // OAuth2 로그인 진행 시 키가 되는 필드 값
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // OAuth2UserService
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        Member member = saveOrUpdate(attributes);
        httpSession.setAttribute("member", new SessionMember(member));


        // 회원 정보 출력
        System.out.println("oAuth2User = " + oAuth2User.getAttributes());

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(member.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());

    }

    // 유저 생성 및 수정 서비스 로직
    private Member saveOrUpdate(OAuthAttributes attributes) {
        Member member = memberRepository.findByEmail(attributes.getEmail());

        if (member == null) {
            member = attributes.toEntity();
        }
        return memberRepository.save(member);
    }
}