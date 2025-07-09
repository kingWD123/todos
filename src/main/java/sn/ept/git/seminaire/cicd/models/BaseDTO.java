package sn.ept.git.seminaire.cicd.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@SuperBuilder
public class BaseDTO implements Serializable {

    private String id;
    @JsonProperty(value = "created_date")
    private LocalDateTime createdDate ;

    @JsonProperty(value = "last_modified_date")
    private LocalDateTime lastModifiedDate;
    private int version;

}
