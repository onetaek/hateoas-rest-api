package com.devframe.domain.comment.entity;

import com.devframe.domain.article.entity.Article;
import com.devframe.global.common.entity.BasicEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
@SQLDelete(sql = "UPDATE comment SET is_deleted = 1, deleted_time = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "is_deleted = false")
public class Comment extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(nullable = false, length = 50)
    private String writer;

    @ManyToOne
    private Article article;

    public Comment update(String title, String content, String writer) {
        if (title != null) this.title = title;
        if (content != null) this.content = content;
        if (writer != null) this.writer = writer;
        return this;
    }
}
