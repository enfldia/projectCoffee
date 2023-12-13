package projectCoffee.dto;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import projectCoffee.entity.Member;

import javax.validation.constraints.*;

@Getter
@Setter
public class MemberUpdateDto {

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

    private String birthday;

    private String address;				// 우편 번호

    private String streetAddress;		// 지번 주소

    private String detailAddress;		// 상세 주소

    private static ModelMapper modelMapper = new ModelMapper();

    public static MemberUpdateDto of(Member member) {
        return modelMapper.map(member, MemberUpdateDto.class);
    }
}
