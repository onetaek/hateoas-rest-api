package com.devframe.domain.article.entity;

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
@SQLDelete(sql = "UPDATE article SET is_deleted = 1, deleted_time = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "is_deleted = false")
public class Article extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(nullable = false, length = 50)
    private String writer;

    @Builder.Default
    private Long views = 0L;

    public Article update(String title, String content) {
        this.title = title;
        this.content = content;
        return this;
    }

    public Article plusViews() {
        this.views++;
        return this;
    }
}
