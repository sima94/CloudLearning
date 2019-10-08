package com.cloudlearning.cloud.models.security;

import javax.persistence.*;

import com.cloudlearning.cloud.models.base.BasicEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name="ROLE", uniqueConstraints = {@UniqueConstraint(columnNames = {"NAME"})})
@Getter
@Setter
@EqualsAndHashCode(of = "id", callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BasicEntity implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "NAME")
    @Getter(AccessLevel.PUBLIC)
    private String name;

    @Override
    @JsonIgnore
    public String getAuthority() { return name; }
}
