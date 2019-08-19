package com.cloudlearning.cloud.models;

import com.cloudlearning.cloud.models.base.BasicEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lessonChapter", fetch = FetchType.LAZY)
    private Collection<LessonComment> lessonComments;

}
