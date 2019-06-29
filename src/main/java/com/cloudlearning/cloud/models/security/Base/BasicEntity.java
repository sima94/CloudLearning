package com.cloudlearning.cloud.models.security.Base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Data
public abstract class BasicEntity<ID extends Long> {

    @Column(name = "IS_DELETED")
    private boolean isDeleted = false;

    @Generated(GenerationTime.INSERT)
    @Basic(optional = false)
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @Generated(GenerationTime.ALWAYS)
    @Column(name = "UPDATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = new Date();

    public abstract ID getId();

    @JsonIgnore
    public boolean isDeleted() {
        return isDeleted;
    }

    @JsonIgnore
    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}