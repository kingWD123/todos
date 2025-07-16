package sn.ept.git.seminaire.cicd.data;

import sn.ept.git.seminaire.cicd.entities.Tag;

public final class TagTestData extends TestData {

    public static Tag defaultTag() {
        return Tag
                .builder()
                .id(Default.id)
                .createdDate(Default.createdDate)
                .lastModifiedDate(Default.lastModifiedDate)
                .version(Default.version)
                .name(Default.name)
                .todos(null)
                .build();
    }

}
