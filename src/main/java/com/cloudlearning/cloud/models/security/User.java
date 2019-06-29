package com.cloudlearning.cloud.models.security;

import com.cloudlearning.cloud.configuration.validation.annotations.FieldsDiversity;
import com.cloudlearning.cloud.configuration.validation.annotations.FieldsEquality;
import com.cloudlearning.cloud.models.security.Base.BasicEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.management.relation.Role;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "USER_", uniqueConstraints = { @UniqueConstraint(columnNames = { "USER_NAME" }) })
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@DynamicUpdate
@FieldsDiversity(groups = {User.ValidationChangePassword.class}, firstFieldName = "password", secondFieldName = "newPassword", message = "api.error.validation.passwordsAreSame")
@FieldsEquality(groups = {User.ValidationChangePassword.class}, firstFieldName = "newPassword", secondFieldName = "confirmNewPassword", message = "api.error.validation.passwordsMismatch")
public class User extends BasicEntity implements UserDetails, Serializable {

    public interface ValidationCreate { }
    public interface ValidationUpdate { }
    public interface ValidationChangePassword { }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;

    @NotNull(groups = {ValidationCreate.class, ValidationUpdate.class}, message = "api.error.validation.email.isRequired")
    @Email(groups = {ValidationCreate.class, ValidationUpdate.class}, message = "api.error.validation.email.failedValidation")
    @Column(name = "USER_NAME")
    private String username;

    @Column(name = "PASSWORD")
    @NotNull(groups = {ValidationCreate.class, ValidationChangePassword.class}, message = "api.error.validation.password.isRequired")
    @Size(groups = {ValidationCreate.class}, min = 6, message = "api.error.validation.password.minSizeLimitation.6")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Transient
    @NotNull(groups = {ValidationChangePassword.class}, message = "api.error.validation.newPassword.isRequired")
    @Size(groups = {ValidationCreate.class, ValidationChangePassword.class}, min = 6, message = "api.error.validation.newPassword.minSizeLimitation.6")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String newPassword;

    @Transient
    @NotNull(groups = {ValidationChangePassword.class}, message = "api.error.validation.confirmNewPassword.isRequired")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String confirmNewPassword;

    @Column(name = "ACCOUNT_EXPIRED")
    private boolean accountExpired;

    @Column(name = "ACCOUNT_LOCKED")
    private boolean accountLocked;

    @Column(name = "CREDENTIALS_EXPIRED")
    private boolean credentialsExpired;

    @Column(name = "ENABLED")
    private boolean enabled;

    private boolean isDeleted = false;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "AUTHORITY_ID")
    @NotNull(groups = {ValidationCreate.class}, message = "api.error.validation.authority.isRequired")
    private Authority authority;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList authorities = new ArrayList<Authority>();
        authorities.add(this.getAuthority());
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
