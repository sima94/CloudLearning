package com.cloudlearning.cloud.models.members;

import com.cloudlearning.cloud.models.Subject;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "PROFESSOR")
@Getter
@Setter
public class Professor extends Member {

    @OneToMany(cascade = CascadeType.DETACH, mappedBy = "professor", fetch = FetchType.LAZY)
    @Where(clause = "IS_DELETED = false")
    private Collection<Subject> subjects;
}
