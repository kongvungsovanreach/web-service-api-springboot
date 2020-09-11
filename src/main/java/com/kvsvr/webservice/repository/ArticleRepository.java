package com.kvsvr.webservice.repository;

import com.kvsvr.webservice.repository.model.Article;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository {


    @Select("SELECT * FROM tb_articles")
    List<Article> findAllArticles();
}
