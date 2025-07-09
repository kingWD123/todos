package sn.ept.git.seminaire.cicd.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 *
 * @author ISENE
 */

@Builder
@JsonPropertyOrder({"status","type","message","description","date","zone","systemId","systemName"})
public record ErrorModel (
        @JsonProperty(value = "system_id")
        String systemId,
        @JsonProperty(value = "system_name")
        String systemName,
        String type,
        int status,
        LocalDateTime date,
        String message,
        String description,
        String zone
) {}
