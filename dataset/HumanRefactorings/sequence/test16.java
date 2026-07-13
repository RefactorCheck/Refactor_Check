public class test16 {
    public static void main(String[] args) {
        Object value = "Hi";

        Number parsedNumber;
        if(value instanceof String) {
            parsedNumber = Long.parseLong("1");
        }
        else {
            parsedNumber = Double.parseDouble("1");
        }

        System.out.println(parsedNumber);
    }
}
