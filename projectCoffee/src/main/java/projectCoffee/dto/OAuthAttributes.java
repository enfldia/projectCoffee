package projectCoffee.dto;

import lombok.Builder;
import lombok.Getter;
import projectCoffee.constant.Role;
import projectCoffee.entity.Member;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String name;
    private final String email;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey,  String name, String email) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
    }

    // OAuth2User 정보를 OAuthAttributes에 입력
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes){
        // kakao
        if("kakao".equals(registrationId)){
            return ofKakao("id", attributes);
        }
        // naver
        if("naver".equals(registrationId)){
            return ofNaver("id", attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    // ofKakao
    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        // kakao는 kakao_account에 유저정보가 있다. (email)
        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
        // kakao_account안에 또 profile이라는 JSON객체가 있다. (nickname, profile_image)
        Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");

        return OAuthAttributes.builder()
                .name((String) kakaoProfile.get("nickname"))
                .email(String.valueOf((Long) attributes.get("id"))) // 멤버 식별을 위해 id값 주입
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // ofNaver
    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        // JSON형태이기 떄문에 Map을 통해서 데이터를 가져온다.
        Map<String, Object> response = (Map<String, Object>)attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("id")) // 멤버 식별을 위해 id값 주입
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }


    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("sub"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // 처음 로그인할 때 OAuthAttributes 에서 엔티티를 생성
    public Member toEntity(){
        Member member = new Member();
        return Member.builder()
                .name(name)
                .email(email)
                .role(Role.USER)
                .build();
    }

}
