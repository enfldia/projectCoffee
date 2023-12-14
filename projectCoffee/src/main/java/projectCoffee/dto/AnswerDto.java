package projectCoffee.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class AnswerDto {
    @NotEmpty(message = "내 용 은 필 수 항 목 입 니 다.")
    private String content;
}
