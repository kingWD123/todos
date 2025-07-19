package sn.ept.git.seminaire.cicd.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import sn.ept.git.seminaire.cicd.utils.SizeMapping;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
public class TagDTO extends BaseDTO {

    @NotBlank
    @Size(min = SizeMapping.Name.MIN,max = SizeMapping.Name.MAX)
    private  String name;

}   