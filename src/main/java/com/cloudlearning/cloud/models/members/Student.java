package com.cloudlearning.cloud.models.members;

import com.cloudlearning.cloud.models.Subject;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "STUDENT")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Student extends Member {

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "STUDENT_SUBJECTS", joinColumns = @JoinColumn(name = "STUDENT_ID", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "SUBJECT_ID", referencedColumnName = "ID"))
    @Where(clause = "IS_DELETED = false")
    private Collection<Subject> subjects;

}
