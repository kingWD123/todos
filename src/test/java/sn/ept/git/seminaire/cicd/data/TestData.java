package sn.ept.git.seminaire.cicd.data;

import org.apache.commons.lang3.RandomStringUtils;
import sn.ept.git.seminaire.cicd.utils.SizeMapping;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.UUID;


public class TestData {

    public TestData(){}

    public static final class Default {
        private Default(){}
        public static final String id = UUID.randomUUID().toString();
        public static final LocalDateTime createdDate = LocalDateTime.now(ZoneOffset.UTC);
        public static final LocalDateTime lastModifiedDate = LocalDateTime.now(ZoneOffset.UTC);
        public static final int version = 0;
        public static final LocalDateTime dateDebut = LocalDateTime.now(ZoneOffset.UTC).plus(30, ChronoUnit.MINUTES);
        public static final LocalDateTime dateFin = LocalDateTime.now(ZoneOffset.UTC).plus(45, ChronoUnit.MINUTES);
        public static final  String title = RandomStringUtils.randomAlphanumeric( (SizeMapping.Title.MIN+SizeMapping.Title.MAX)/2);
        public static final  String name = RandomStringUtils.randomAlphanumeric( (SizeMapping.Name.MIN+SizeMapping.Name.MAX)/2);
        public static final  String description=  RandomStringUtils.randomAlphanumeric( (SizeMapping.Description.MIN+SizeMapping.Description.MAX)/2);
    }


    public static final class Update {
        private Update(){}
        public static final UUID id = UUID.randomUUID();
        public static final LocalDateTime createdDate = LocalDateTime.now(ZoneOffset.UTC);
        public static final LocalDateTime lastModifiedDate = LocalDateTime.now(ZoneOffset.UTC);
        public static final int version = 2;
        public static final LocalDateTime dateDebut = LocalDateTime.now(ZoneOffset.UTC).plus(30, ChronoUnit.MINUTES);
        public static final LocalDateTime dateFin = LocalDateTime.now(ZoneOffset.UTC).plus(45, ChronoUnit.MINUTES);
        public static final  String title = RandomStringUtils.randomAlphanumeric( (SizeMapping.Title.MIN+SizeMapping.Title.MAX)/2);
        public static final  String name = RandomStringUtils.randomAlphanumeric( (SizeMapping.Name.MIN+SizeMapping.Name.MAX)/2);
        public static final  String description=  RandomStringUtils.randomAlphanumeric( (SizeMapping.Description.MIN+SizeMapping.Description.MAX)/2);
    }

}
