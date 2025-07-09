package sn.ept.git.seminaire.cicd.data;


import sn.ept.git.seminaire.cicd.entities.Todo;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


public final class TodoTestData extends TestData {

    public static Todo defaultTodo() {
        return Todo
                .builder()
                .id(Default.id)
                .createdDate(Default.createdDate)
                .lastModifiedDate(Default.lastModifiedDate)
                .version(Default.version)
                .title(Default.title)
                .description(Default.description)
                .dateDebut(LocalDateTime.now().plus(45, ChronoUnit.MINUTES))
                .dateFin(LocalDateTime.now().plus(55, ChronoUnit.MINUTES))
                .tags(null)
                .build();
    }

}
