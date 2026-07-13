public class test25 {
    private static final String DEFAULT_TEXT = "foo";
    private final JTextField textField = new JTextField();

    public void onValueSet(ValueSetEvent<?> event) {
        this.textField.setText(DEFAULT_TEXT);
    }
}
