package projectCoffee.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;
import projectCoffee.constant.Role;
import projectCoffee.dto.MemberEditDto;
import projectCoffee.dto.MemberFormDto;
import projectCoffee.repository.MemberRepository;

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
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        member.setRole(Role.ADMIN);
        return member;
    }



}
