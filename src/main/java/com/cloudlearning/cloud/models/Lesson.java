package com.cloudlearning.cloud.models;

import com.cloudlearning.cloud.models.base.BasicEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
public class Lesson extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "INTRO")
    private String intro;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lesson", fetch = FetchType.LAZY)
    private Collection<LessonChapter> lessonChapters;

}
