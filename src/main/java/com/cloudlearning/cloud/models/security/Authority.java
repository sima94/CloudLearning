package com.cloudlearning.cloud.models.security;

import javax.persistence.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name="AUTHORITY", uniqueConstraints = {@UniqueConstraint(columnNames = {"NAME"})})
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Override
    public String getAuthority() { return name; }
}
