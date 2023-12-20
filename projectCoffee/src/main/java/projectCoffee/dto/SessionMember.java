package projectCoffee.dto;

import lombok.Getter;
import projectCoffee.entity.Member;

@Getter
public class SessionMember {
    private final Long id;
    private final String name;
    private final String email;
    private final String phoneNum;
    private final String birthday;
    private final String zipCode;
    private final String streetAddress;
    private final String detailAddress;


    public SessionMember(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.phoneNum = member.getPhoneNum();
        this.birthday = member.getBirthday();
        this.zipCode = member.getZipCode();
        this.streetAddress = member.getStreetAddress();
        this.detailAddress = member.getDetailAddress();

    }
}
