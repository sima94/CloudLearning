package com.cloudlearning.cloud.models;

import com.cloudlearning.cloud.models.base.BasicEntity;
import com.cloudlearning.cloud.models.members.Professor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
public class Subject extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToOne(mappedBy = "subject", cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Professor professor;

    private Collection<Lesson> lessonsCollection;

}
