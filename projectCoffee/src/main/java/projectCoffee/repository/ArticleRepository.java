package projectCoffee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import projectCoffee.entity.Article;

import java.util.ArrayList;

public interface ArticleRepository extends JpaRepository<Article, Long>, QuerydslPredicateExecutor<Article>, ArticleRepositoryCustom {
    @Override
    ArrayList<Article> findAll();
}
