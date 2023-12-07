package projectCoffee.dto;

import lombok.Getter;
import lombok.Setter;
import projectCoffee.constant.ArticleType;

@Getter @Setter
public class ArticleSearchDto {

    private String searchDateType;

    private ArticleType searchArticleType;

    private String searchBy;

    private String searchQuery="";
}
