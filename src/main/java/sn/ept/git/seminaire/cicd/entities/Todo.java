package sn.ept.git.seminaire.cicd.entities;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import sn.ept.git.seminaire.cicd.utils.SizeMapping;
import lombok.experimental.SuperBuilder;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@SuperBuilder
@Getter
@Setter
@EqualsAndHashCode (callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "acicd_todos")
@DynamicUpdate
public final class Todo extends BaseEntity implements Serializable {

    @NotBlank
    @Size(min = SizeMapping.Title.MIN, max = SizeMapping.Title.MAX)
    private String title;

    @Size(min = SizeMapping.Description.MIN, max = SizeMapping.Description.MAX)
    private String description;

    private boolean completed;

    @NotNull
    @Future
    private LocalDateTime dateDebut;

    @NotNull
    @Future
    private LocalDateTime dateFin;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "tags_todos",
            joinColumns = @JoinColumn(name = "id_todo"),
            inverseJoinColumns = @JoinColumn(name = "id_tag")
    )
    private Set<Tag> tags = new HashSet<>();


}
