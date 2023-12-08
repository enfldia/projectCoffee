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
public class Member {
    @Id
    @Column(name = "member_num")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long num;

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
