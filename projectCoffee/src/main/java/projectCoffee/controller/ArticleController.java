package projectCoffee.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projectCoffee.dto.ArticleDto;
import projectCoffee.dto.ArticleSearchDto;
import projectCoffee.entity.Article;
import projectCoffee.repository.ArticleRepository;
import projectCoffee.service.ArticleService;


import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleRepository articleRepository;

    @GetMapping(value = "/CsCenter/faq")
    public String faq(Model model) {
        model.addAttribute("headerImg","/images/header.jpg");
        return "cscenter/faq";
    }

    //공지사항 게시판
    @GetMapping(value = {"/CsCenter/notices","/CsCenter/notices/{page}"})
    public String notice(ArticleSearchDto articleSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<Article> articles = articleService.getArticlePage(articleSearchDto,pageable);
        model.addAttribute("articleList", articles);
        model.addAttribute("articleSearchDto",articleSearchDto);
        model.addAttribute("maxPage",5);
        return "cscenter/notice";
    }
    //공지사항 상세 페이지
    @GetMapping("/CsCenter/notice/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        Article article = articleRepository.findById(id).orElse(null);
        //model.addAttribute("headerImg", "/images/header.jpg");
        model.addAttribute("article", article);
        model.addAttribute("createdBy", article.getCreatedBy());
        return "article/show";
    }
    //관리자 공지사항 게시판
    @GetMapping(value = {"/admin/notices", "/admin/notices/{page}"})
    public String adminNotice(ArticleSearchDto articleSearchDto,
                              @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<Article> articles = articleService.getArticlePage(articleSearchDto, pageable);
        //model.addAttribute("headerImg", "/images/header.jpg");
        model.addAttribute("articleList", articles);
        model.addAttribute("articleSearchDto", articleSearchDto);
        model.addAttribute("maxPage", 5);
        return "article/adminArticleList";
    }
    //관리자 페이지에서 게시글 상세 확인
    @GetMapping("/admin/notice/{id}")
    public String adminShow(@PathVariable("id") Long id, Model model) {
        Article article = articleRepository.findById(id).orElse(null);
        //model.addAttribute("headerImg", "/images/header.jpg");
        model.addAttribute("article", article);
        model.addAttribute("createBy", article.getCreatedBy());
        return "article/adminShow";
    }
    //관리자 페이지에서 공지 작성 페이지
    @GetMapping("/admin/notice/create")
    public String newArticleForm(Model model) {
        //model.addAttribute("headerImg", "/images/header.jpg");
        model.addAttribute("articleDto", new Article());
        return "article/new";
    }
    //관리자 페이지에서 공지 작성 페이지
    @PostMapping("/admin/notice/create")
    public String createArticle(@Valid ArticleDto articleDto, BindingResult bindingResult,
                                Model model) {
        if(bindingResult.hasErrors()) {
            return "article/new";
        }
        try {
            Article article = Article.createArticle(articleDto);
            articleRepository.save(article);
            return "redirect:/admin/notices";
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "article/new";
        }
    }
    //관리자 페이지에서 게시글 수정
    @GetMapping("/admin/notice/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Article article = articleRepository.findById(id).orElse(null);
        //model.addAttribute("headerImg", "/images/header.jpg");
        model.addAttribute("article", article);
        return "article/edit";
    }
    //관리자 페이지에서 공지 수정 폼 전송
    @PostMapping("/admin/notice/update")
    public String update(@Valid ArticleDto articleDto,
                         BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            return "article/adminArticleList";
        }
        try {
            Optional<Article> existingArticleOptional = articleRepository.findById(articleDto.getId());
            if(existingArticleOptional.isPresent()) {
                Article existingArticle = existingArticleOptional.get();
                existingArticle.setTitle(articleDto.getTitle());
                existingArticle.setContent(articleDto.getContent());
                articleRepository.save(existingArticle);
//                return "redirect:/admin/notices/" + existingArticle.getId();
                return "redirect:/admin/notices";
            } else {
                model.addAttribute("errorMessage", "해당 글을 찾을 수 없습니다.");
                return "article/adminArticleList";
            }
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "article/adminArticleList";
        }
    }
    //관리자 페이지에서 공지 삭제하기(단수 삭제)
    @GetMapping("/admin/notice/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr) {
        Article target = articleRepository.findById(id).orElse(null);
        if(target!=null) {
            articleRepository.delete(target);
            rttr.addFlashAttribute("msg", "삭제되었습니다.");
        }
        return "redirect:/admin/notices";
    }
    //관리자 페이지에서 공지 삭제하기(복수 삭제)
    @PostMapping("admin/notices/delete")
    public String deleteSelectedArticles(@RequestParam("selectedArticles")
                                             List<Long> selectedArticles) {
        articleService.deleteArticles(selectedArticles);

        return "redirect:/admin/notices";
    }
}
