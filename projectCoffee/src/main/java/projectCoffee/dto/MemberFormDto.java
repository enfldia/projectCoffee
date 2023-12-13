package projectCoffee.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import projectCoffee.entity.Member;

import javax.validation.constraints.*;

@Getter
@Setter
public class MemberFormDto {

    private Long id;

    @NotBlank
    private String name;

    @NotEmpty
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^[0-9]*$", message = "정수만 가능합니다.")
    @Size(min = 10, max = 11, message = "다시 한 번 확인해주세요.")
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
