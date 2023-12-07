package projectCoffee.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;
import projectCoffee.constant.Role;
import projectCoffee.dto.MemberFormDto;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "member")
//Auditing 기능을 적용하기 위해 BaseEntity 상속시킴 12/6 상현

public class Member extends BaseEntity{

    @Id
    @Column(name = "member_num")
    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long num;
    private Long Id; //교재에는 id 로 되어있어서 바꿨습니다. - 상현


    private String name;

    private String userId;

    private String password;

    private String email;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static  Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setUserId(memberFormDto.getUserId());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        member.setRole(Role.ADMIN);
        return member;
    }

}
