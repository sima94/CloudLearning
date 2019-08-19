package com.cloudlearning.cloud.models.members;

import com.cloudlearning.cloud.models.Subject;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Collection;

public class Professor extends Member {

    @OneToMany(cascade = CascadeType.DETACH, mappedBy = "professor", fetch = FetchType.LAZY)
    private Collection<Subject> subjects;
}
