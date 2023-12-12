package projectCoffee.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class MemberFormDto {

    @NotBlank
    private String name;

    @NotEmpty
    @Email
    private String email;

    @NotBlank
    private String phoneNum;

    @NotEmpty
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{8,20}", message = "비밀번호는 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    private String password;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password_confirm;

    private String birthday;

    private String address;				// 우편 번호

    private String streetAddress;		// 지번 주소

    private String detailAddress;		// 상세 주소
}
