package projectCoffee.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import projectCoffee.dto.ArticleSearchDto;
import projectCoffee.entity.Article;


public interface ArticleRepositoryCustom {

    Page<Article> getArticlePage(ArticleSearchDto articleSearchDto, Pageable pageable);

}
