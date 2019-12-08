package com.cloudlearning.cloud.models.members.student;

import com.cloudlearning.cloud.models.Subject;
import com.cloudlearning.cloud.models.members.Member;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "STUDENT")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Student extends Member {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "STUDENT_SUBJECTS", joinColumns = @JoinColumn(name = "STUDENT_ID", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "SUBJECT_ID", referencedColumnName = "ID"))
    @Where(clause = "IS_DELETED = false")
    private Collection<Subject> subjects;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @Where(clause = "IS_DELETED = false")
    private Set<StudentSubject> studentSubjects;

}
