package com.froggy.common;

import com.froggy.common.util.DateUtil;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class DateTimeEntity {

    @Column(updatable = false)
    protected LocalDateTime createdAt;

    @Column
    protected LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = DateUtil.now();
        this.updatedAt = DateUtil.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = DateUtil.now();
    }

}
