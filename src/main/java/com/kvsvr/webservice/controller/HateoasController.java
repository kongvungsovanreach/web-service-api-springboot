package com.kvsvr.webservice.controller;

import com.kvsvr.webservice.repository.ArticleRepository;
import com.kvsvr.webservice.repository.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.config.EnableEntityLinks;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@ExposesResourceFor(Article.class)
@RequestMapping("/hateoas")
@EnableEntityLinks
public class HateoasController {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private EntityLinks entityLinks;

    @GetMapping(value = "/articles", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Article> getAllArticles() {
        List<Article> articles = articleRepository.findAllArticles();
        for (Article article : articles) {
//            Link link = entityLinks.linkToSingleResource(Article.class, article.getArticleId());
            Link link = entityLinks.linkToCollectionResource(article.getClass());
            article.add(link);
        }


        return articles;
    }
}
