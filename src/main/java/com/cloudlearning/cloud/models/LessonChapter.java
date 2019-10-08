package com.cloudlearning.cloud.models;

import com.cloudlearning.cloud.models.base.BasicEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "LESSON_CHAPTER")
@Getter
@Setter
public class LessonChapter extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "TEXT")
    private String text;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "LESSON_ID")
    @Where(clause = "IS_DELETED = false")
    private Lesson lesson;

    @OneToMany(mappedBy = "lessonChapter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Where(clause = "IS_DELETED = false")
    private Collection<LessonComment> lessonComments;

}
