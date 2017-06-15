package pl.java.scalatech;

public enum AppProfile {

    PROD("prod"), STAGE("stage"), DEV("dev"), TEST("test");

    String value;

    private AppProfile(String value) {
        this.value = value;
    }

}
