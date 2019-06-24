package com.cloudlearning.cloud.models.security;

import javax.persistence.*;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name="AUTHORITY", uniqueConstraints = {@UniqueConstraint(columnNames = {"NAME"})})
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "NAME")
    @Getter(AccessLevel.PRIVATE)
    private String name;

    @Override
    public String getAuthority() { return name; }
}
