package com.devframe.domain.comment.repository;

import com.devframe.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentCommandRepository extends JpaRepository<Comment, Long> {
}
