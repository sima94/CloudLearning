package com.cloudlearning.cloud.models;

import com.cloudlearning.cloud.models.base.BasicEntity;
import com.cloudlearning.cloud.models.members.Professor;
import com.cloudlearning.cloud.models.members.Student;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "SUBJECT")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Subject extends BasicEntity {

    public interface ValidationCreate { }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;


    @NotNull(groups = {ValidationCreate.class}, message = "api.error.validation.name.isRequired")
    @Size(min = 3, message = "api.error.validation.name.minSizeLimitation.3")
    @Column(name = "NAME")
    private String name;

    @NotNull(groups = {ValidationCreate.class}, message = "api.error.validation.description.isRequired")
    @Size(min = 3, message = "api.error.validation.description.minSizeLimitation.3")
    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Professor professor;

    @NotNull(groups = {ValidationCreate.class}, message = "api.error.validation.professorId.isRequired")
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long professorId;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "STUDENT_SUBJECTS", joinColumns = @JoinColumn(name = "SUBJECT_ID", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "STUDENT_ID", referencedColumnName = "ID"))
    private Set<Student> students;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Where(clause = "IS_DELETED = false")
    private Collection<Lesson> lessonsCollection;

}
