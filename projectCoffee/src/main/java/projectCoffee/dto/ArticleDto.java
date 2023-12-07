package projectCoffee.dto;

import lombok.Getter;
import lombok.Setter;
import projectCoffee.constant.ArticleType;
import projectCoffee.entity.Article;


import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class ArticleDto {
    public Long id;
    @NotEmpty(message = "제목은 필수 입력값입니다.")
    private String title;
    @NotEmpty(message = "내용은 필수 입력값입니다.")
    private String content;
    private ArticleType articleType;

    public Article toEntity() {
        return new Article(id, title, content, articleType);
    }
}
