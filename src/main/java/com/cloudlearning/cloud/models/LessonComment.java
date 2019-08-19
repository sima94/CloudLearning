package com.cloudlearning.cloud.models;

import com.cloudlearning.cloud.models.base.BasicEntity;
import com.cloudlearning.cloud.models.members.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
public class LessonComment extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;

    @Column(name = "TEXT")
    private String text;

    @OneToOne(mappedBy = "lessonComment", cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Member member;

    @OneToOne(mappedBy = "lessonComment", cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private LessonChapter lessonChapter;

    @OneToOne(mappedBy = "lessonComment", cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private LessonComment parentLessonComment;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lessonComment", fetch = FetchType.LAZY)
    private Collection<LessonComment> replayComments;

}
