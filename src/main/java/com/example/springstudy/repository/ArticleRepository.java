package com.example.springstudy.repository;

import com.example.springstudy.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

  @Query("select distinct A from Article A left join fetch A.articleComments")
  List<Article> findAllJoinFetch();
}
