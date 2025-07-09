package sn.ept.git.seminaire.cicd.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import sn.ept.git.seminaire.cicd.utils.SizeMapping;
import sn.ept.git.seminaire.cicd.validators.DateRangeValid;

import java.time.LocalDateTime;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
@DateRangeValid(message = "Field dateDebut should be before dateFin")
public class TodoDTO extends BaseDTO {

    @NotBlank(message = "title is required")
    @Size(
            min = SizeMapping.Title.MIN,
            max = SizeMapping.Title.MAX,
            message = "title must be between " + SizeMapping.Title.MIN + " and " + SizeMapping.Title.MAX + " characters"
    )
    private String title;

    @Size(
            min = SizeMapping.Description.MIN,
            max = SizeMapping.Description.MAX,
            message = "description must not exceed "+ SizeMapping.Description.MAX + " characters"
    )
    private String description;

    private boolean completed;

    @NotNull(message = "dateDebut is required")
    @Future(message = "dateDebut must be in the future")
    @JsonProperty(value = "date_debut")
    private LocalDateTime dateDebut;

    @NotNull(message = "dateFin is required")
    @Future(message = "dateFin must be in the future")
    @JsonProperty(value = "date_fin")
    private LocalDateTime dateFin;

    @JsonIgnore
    private Set<TagDTO> tags;
}