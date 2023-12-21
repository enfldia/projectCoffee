package projectCoffee.entity;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import projectCoffee.constant.Role;
import projectCoffee.dto.MemberFormDto;
import projectCoffee.dto.MemberUpdateDto;
import projectCoffee.dto.OAuthAttributes;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "member")
@NoArgsConstructor
public class Member extends BaseEntity{

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    private String phoneNum;

    private String birthday;

    private String zipCode;				// 우편 번호

    private String streetAddress;		// 지번 주소

    private String detailAddress;		// 상세 주소

    @Enumerated(EnumType.STRING)
    private Role role;


    @Builder
    public Member(String name, String email, String password,String phoneNum, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNum = phoneNum;
        this.role = role;

    }

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setPhoneNum(memberFormDto.getPhoneNum());
        member.setBirthday(memberFormDto.getBirthday());
        member.setZipCode(memberFormDto.getZipCode());
        member.setStreetAddress(memberFormDto.getStreetAddress());
        member.setDetailAddress(memberFormDto.getDetailAddress());
        member.setRole(Role.ADMIN);
        return member;
    }


    public void updateMember(MemberUpdateDto memberUpdateDto) {
        this.id= memberUpdateDto.getId();
        this.name = memberUpdateDto.getName();
        this.birthday = memberUpdateDto.getBirthday();
        this.phoneNum = memberUpdateDto.getPhoneNum();
        this.zipCode = memberUpdateDto.getZipCode();
        this.streetAddress = memberUpdateDto.getStreetAddress();
        this.detailAddress = memberUpdateDto.getDetailAddress();

    }

  //  public Member update(String email){
  //      this.email = email;
  //      return this;
  //  }

    public String getRoleKey(){
        return this.role.getKey();
    }

}
