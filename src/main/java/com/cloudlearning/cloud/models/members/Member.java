package com.cloudlearning.cloud.models.members;

import com.cloudlearning.cloud.models.base.BasicEntity;
import com.cloudlearning.cloud.models.security.User;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MEMBER")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public class Member extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "DATE_OF_BIRTH")
    private Date dateOfBirth;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

}
