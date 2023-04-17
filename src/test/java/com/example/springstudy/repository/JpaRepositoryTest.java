package com.example.springstudy.repository;

import com.example.springstudy.domain.Article;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@DisplayName("JPA 연결 테스트")
@DataJpaTest
class JpaRepositoryTest {

  private final ArticleRepository articleRepository;


  JpaRepositoryTest(@Autowired ArticleRepository articleRepository) {
    this.articleRepository = articleRepository;
  }



  @Test
  void JPA_게시글_SELECT() {
    // Given

    // When
    List<Article> articles = articleRepository.findAll();
    // Then
    assertThat(articles)
        .isNotNull();
  }

  @Test
  void JPA_게시글_INSERT() {
    // Given
    long previousCount = articleRepository.count();
    Article article = Article.of("테스트 제목", "테스트 내용", "김건", "gunkim");
    // When
    Article savedArticle = articleRepository.save(article);
    // Then
    assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
  }


}
