package sn.ept.git.seminaire.cicd.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@ToString
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract sealed class BaseEntity implements Serializable permits Tag, Todo {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Builder.Default
    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate =LocalDateTime.now(ZoneOffset.UTC) ;

    @Builder.Default
    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate=LocalDateTime.now(ZoneOffset.UTC);

    @Version
    private int version;

    @PrePersist
    void prePersit(){
        this.setCreatedDate(LocalDateTime.now(ZoneOffset.UTC));
        this.setLastModifiedDate(LocalDateTime.now(ZoneOffset.UTC));
    }

    @PreUpdate
    void preUpdate(){
        this.setLastModifiedDate(LocalDateTime.now(ZoneOffset.UTC));
    }

}