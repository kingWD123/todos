package sn.ept.git.seminaire.cicd.utils;

/**
 * @author ISENE
 */
public final class SizeMapping {

    private SizeMapping() {
        super();
    }


    public static final class Name {
        private Name(){
            super();
        }
        public static final int MIN=2;
        public static final int MAX=50;
    }

    public static final class Email {
        private Email(){
            super();
        }
        public static final int MIN=5;
        public static final int MAX=50;
    }

    public static final class Phone {
        private Phone(){
            super();
        }
        public static final int MIN=5;
        public static final int MAX=20;
    }

    public static final class Adresse {
        private Adresse(){
            super();
        }
        public static final int MIN=0;
        public static final int MAX=512;
    }

    public static final class Description {
        private Description(){
            super();
        }
        public static final int MIN=0;
        public static final int MAX=255;
    }


    public static final class Comment {
        private Comment(){
            super();
        }
        public static final int MIN=0;
        public static final int MAX=255;
    }

    public static final class Location {
        private Location(){
            super();
        }
        public static final int MIN=0;
        public static final int MAX=255;
    }

    public static final class Hash {
        private Hash(){
            super();
        }
        public static final int MIN=8;
        public static final int MAX=255;
    }

    public static final class Title {
        private Title(){
            super();
        }
        public static final int MIN=2;
        public static final int MAX=80;
    }
}