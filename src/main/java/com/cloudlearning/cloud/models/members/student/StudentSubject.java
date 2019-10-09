package com.cloudlearning.cloud.models.members.student;

import com.cloudlearning.cloud.models.Subject;
import com.cloudlearning.cloud.models.base.BasicEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "STUDENT_SUBJECTS")
@Getter
@Setter
@IdClass(StudentSubject.IdClass.class)
public class StudentSubject extends BasicEntity {

    @Column(name = "ID")
    private Long id;

    @Data
    static class IdClass implements Serializable {
        private Long studentId;
        private Long subjectId;
    }

    @Id
    @Column(name = "STUDENT_ID", insertable = false, updatable = false)
    private Long studentId;

    @Id
    @Column(name = "SUBJECT_ID", insertable = false, updatable = false)
    private Long subjectId;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "STUDENT_ID")
    @Where(clause = "IS_DELETED = false")
    private Student student;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "SUBJECT_ID")
    @Where(clause = "IS_DELETED = false")
    private Subject subject;

    @Column(name = "IS_APPROVED")
    private Boolean isApproved = false;

}