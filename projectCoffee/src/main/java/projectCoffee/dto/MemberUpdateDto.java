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
    @Pattern(regexp = "^(01[1|6|7|8|9|0])-(\\d{3,4})-(\\d{4})$", message = "01x-xxxx-xxxx의 형식으로 작성해주세요")
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
