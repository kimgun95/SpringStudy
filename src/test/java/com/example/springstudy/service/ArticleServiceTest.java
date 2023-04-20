package com.example.springstudy.service;

import com.example.springstudy.dto.response.ArticleResponse;
import com.example.springstudy.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - Article")
@WebMvcTest(MockitoExtension.class)
@MockBean(JpaMetamodelMappingContext.class)
class ArticleServiceTest {

  @InjectMocks private ArticleService sut;
  @Mock private ArticleRepository articleRepository;

  @Test
  void 게시글_리스트_조회() {
    // Given
    given(sut.searchArticles()).willReturn(List.of());
    // When
    List<ArticleResponse> articles = sut.searchArticles();
    // Then
    assertThat(articles).isEmpty();
  }


}