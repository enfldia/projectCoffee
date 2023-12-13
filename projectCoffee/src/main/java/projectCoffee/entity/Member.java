package projectCoffee.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;
import projectCoffee.constant.Role;
import projectCoffee.dto.MemberFormDto;
import projectCoffee.dto.MemberUpdateDto;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "member")
public class Member extends BaseEntity{

    @Id
    @Column(name = "member_num")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String email;

    private String password;

    private String phoneNum;

    private String birthday;

    private String address;				// 우편 번호

    private String streetAddress;		// 지번 주소

    private String detailAddress;		// 상세 주소

    @Enumerated(EnumType.STRING)
    private Role role;


    public static  Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setPhoneNum(memberFormDto.getPhoneNum());
        member.setBirthday(memberFormDto.getBirthday());
        member.setAddress(memberFormDto.getAddress());
        member.setStreetAddress(memberFormDto.getStreetAddress());
        member.setDetailAddress(memberFormDto.getDetailAddress());
        member.setRole(Role.ADMIN);
        return member;
    }


    public void updateMember(MemberUpdateDto memberUpdateDto) {
        this.name = memberUpdateDto.getName();
        this.birthday = memberUpdateDto.getBirthday();
        this.phoneNum = memberUpdateDto.getPhoneNum();
        this.address = memberUpdateDto.getAddress();
        this.streetAddress = memberUpdateDto.getStreetAddress();
        this.detailAddress = memberUpdateDto.getDetailAddress();
    }




}
