package projectCoffee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
    @Pattern(regexp = "^(01[1|6|7|8|9|0])-(\\d{3,4})-(\\d{4})$", message = "01x-xxxx-xxxx의 형식으로 작성해주세요")
    private String phoneNum;

    @NotEmpty
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{8,20}", message = "비밀번호는 영어, 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    private String password;

    @NotEmpty
    private String passwordConfirm;

    private String birthday;

    private String zipCode;				// 우편 번호

    private String streetAddress;		// 지번 주소

    private String detailAddress;		// 상세 주소

}
