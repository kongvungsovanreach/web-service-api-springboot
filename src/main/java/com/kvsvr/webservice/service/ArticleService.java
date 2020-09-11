package com.kvsvr.webservice.service;

import com.kvsvr.webservice.repository.ArticleRepository;
import com.kvsvr.webservice.repository.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> findAllArticles() {
        return articleRepository.findAllArticles();
    }
}
