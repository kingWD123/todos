package sn.ept.git.seminaire.cicd.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import sn.ept.git.seminaire.cicd.models.TodoDTO;

import java.util.Objects;

public class DateRangeValidator implements ConstraintValidator<DateRangeValid, TodoDTO> {

    @Override
    public boolean isValid(TodoDTO dto, ConstraintValidatorContext context) {
        return Objects.nonNull(dto) &&
                        Objects.nonNull(dto.getDateDebut()) &&
                        Objects.nonNull(dto.getDateFin()) &&
                        dto.getDateDebut().isBefore(dto.getDateFin());
    }
}