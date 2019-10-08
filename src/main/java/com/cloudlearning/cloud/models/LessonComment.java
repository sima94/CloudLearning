package com.cloudlearning.cloud.models;

import com.cloudlearning.cloud.models.base.BasicEntity;
import com.cloudlearning.cloud.models.members.Member;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "LESSON_COMMENT")
@Getter
@Setter
public class LessonComment extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;

    @Column(name = "TEXT")
    private String text;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Member member;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "LESSON_CHAPTER_ID")
    @Where(clause = "IS_DELETED = false")
    private LessonChapter lessonChapter;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "LESSON_COMMENT_ID")
    @Where(clause = "IS_DELETED = false")
    private LessonComment lessonComment;

    @OneToMany(mappedBy = "lessonComment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Where(clause = "IS_DELETED = false")
    private Collection<LessonComment> lessonComments;

}
