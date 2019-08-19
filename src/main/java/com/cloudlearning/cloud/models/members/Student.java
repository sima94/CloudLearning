package com.cloudlearning.cloud.models.members;

import com.cloudlearning.cloud.models.Subject;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import java.util.Collection;

public class Student extends Member {

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "STUDENT_SUBJECTS", joinColumns = @JoinColumn(name = "STUDENT_ID", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "SUBJECT_ID", referencedColumnName = "ID"))
    private Collection<Subject> subjects;

}
