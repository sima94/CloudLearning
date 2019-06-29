package com.cloudlearning.cloud.models.security;

import javax.persistence.*;

import com.cloudlearning.cloud.models.security.Base.BasicEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name="AUTHORITY", uniqueConstraints = {@UniqueConstraint(columnNames = {"NAME"})})
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class Authority extends BasicEntity implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "NAME")
    @Getter(AccessLevel.PRIVATE)
    private String name;

    @Override
    @JsonIgnore
    public String getAuthority() { return name; }
}
