package projectCoffee.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;
import projectCoffee.constant.ArticleType;
import projectCoffee.dto.ArticleSearchDto;
import projectCoffee.entity.Article;
import projectCoffee.entity.QArticle;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom {

    public JPAQueryFactory queryFactory;

    public ArticleRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchArticleTypeEq(ArticleType searchArticleType) {
        return searchArticleType == null ? null : QArticle.article.articleType.eq(searchArticleType);
    }

    private BooleanExpression regDtsAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now();
        if(StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if(StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1);
        } else if(StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if(StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if(StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }
        return QArticle.article.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if(StringUtils.equals("title", searchBy)) {
            return QArticle.article.title.like("%"+searchQuery+"%");
        } else if(StringUtils.equals("createBy", searchBy)) {
            return QArticle.article.createdBy.like("%"+searchQuery+"%");
        }
        return null;
    }

    @Override
    public Page<Article> getArticlePage(ArticleSearchDto articleSearchDto, Pageable pageable) {
        List<Article> content = queryFactory
                .selectFrom(QArticle.article)
                .where(regDtsAfter(articleSearchDto.getSearchDateType()),
                        searchArticleTypeEq(articleSearchDto.getSearchArticleType()),
                        searchByLike(articleSearchDto.getSearchBy(),
                                articleSearchDto.getSearchQuery()))
                .orderBy(QArticle.article.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory.select(Wildcard.count).from(QArticle.article)
                .where(regDtsAfter(articleSearchDto.getSearchDateType()),
                        searchArticleTypeEq(articleSearchDto.getSearchArticleType()),
                        searchByLike(articleSearchDto.getSearchBy(), articleSearchDto.getSearchQuery()))
                .fetchOne();
        return new PageImpl<>(content, pageable, total);
    }
}