package projectCoffee.service;

import lombok.RequiredArgsConstructor;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import projectCoffee.repository.MemberRepository;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2MemberService extends DefaultOAuth2UserService {

    private MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // OAuth 서비스(google, naver, kakao)에서 가져온 유저 정보를 담고있음
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // OAuth 서비스 이름
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // OAuth 로그인 시 키 값. 구글, 네이버, 카카오 등 각 다르기 때문에 변수로 받아서 넣음
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2 서비스의 유저 정보들
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // registrationId에 따라 유저 정보를 통해 공통된 UserProfile 객체로 만들어 줌



        // 회원 정보 출력
        System.out.println("oAuth2User = " + oAuth2User.getAttributes());
       return super.loadUser(userRequest);


    }



    }




