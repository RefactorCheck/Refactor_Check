/* Constants.java */
class Constants {
}

/* FooConstants.java */
class FooConstants {
    public static final String FOO = "FOO";
}

/* Example.java */
public class test34 {
    public method example(String value) {
        switch (value) {
            case FooConstants.FOO:
                break;
        }
    }
}
