package sn.ept.git.seminaire.cicd.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import sn.ept.git.seminaire.cicd.utils.SizeMapping;
import lombok.experimental.SuperBuilder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@EqualsAndHashCode (callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "acicd_tags")
@DynamicUpdate
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Tag extends BaseEntity implements Serializable {

    @NotBlank
    @Size(min = SizeMapping.Name.MIN, max = SizeMapping.Name.MAX)
    @Column(unique = true)
    private String name;

    @ToString.Exclude
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Todo> todos = new HashSet<>();

}
