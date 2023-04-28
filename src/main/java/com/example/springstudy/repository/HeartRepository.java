package com.example.springstudy.repository;

import com.example.springstudy.domain.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {
  Optional<Heart> findByUserIdAndArticleId(Long userId, Long articleId);
  Optional<Heart> findByUserIdAndArticleIdAndCommentId(Long userId, Long articleId, Long commentId);
  void deleteByUserIdAndArticleId(Long userId, Long articleId);
  void deleteByUserIdAndArticleIdAndCommentId(Long userId, Long articleId, Long commentId);
}
