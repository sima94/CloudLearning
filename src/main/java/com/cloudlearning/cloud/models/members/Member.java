package com.cloudlearning.cloud.models.members;

import com.cloudlearning.cloud.models.base.BasicEntity;
import com.cloudlearning.cloud.models.security.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "MEMBER")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Member extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull(message = "api.error.validation.firstName.isRequired")
    @Size(min = 3, message = "api.error.validation.firstName.minSizeLimitation.3")
    @Column(name = "FIRST_NAME")
    private String firstName;

    @NotNull(message = "api.error.validation.lastName.isRequired")
    @Column(name = "LAST_NAME")
    @Size(min = 3, message = "api.error.validation.lastName.minSizeLimitation.3")
    private String lastName;

    @Column(name = "DATE_OF_BIRTH")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    @Where(clause = "IS_DELETED = false")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;


    public void remapFrom(Member member) {
        if (this != member){
            this.setFirstName(member.getFirstName());
            this.setLastName(member.getLastName());
            this.setDateOfBirth(member.getDateOfBirth());
        }
    }

}
