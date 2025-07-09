package sn.ept.git.seminaire.cicd.data;

import sn.ept.git.seminaire.cicd.models.TagDTO;

public final class TagDTOTestData extends TestData {

    public static TagDTO defaultDTO() {
        return TagDTO
                .builder()
                .id(Default.id)
                .createdDate(Default.createdDate)
                .lastModifiedDate(Default.lastModifiedDate)
                .version(Default.version)
                .name(Default.name)
                .build();
    }

    public static TagDTO updatedDTO() {
        return TagDTO
                .builder()
                .id(Default.id)
                .createdDate(Update.createdDate)
                .lastModifiedDate(Update.lastModifiedDate)
                .version(Update.version)
                .name(Update.name)
                .build();
    }
}
