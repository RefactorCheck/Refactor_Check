public class nacos_0162 {

    private static final String ALL_CHARACTERS = LOWER_CASE + UPPER_CASE + DIGITS + SPECIAL_CHARS;

    public static String generateRandomPassword() {
        SecureRandom random = new SecureRandom();

        List<Character> pwdChars = new ArrayList<>();

        pwdChars.add(LOWER_CASE.charAt(random.nextInt(LOWER_CASE.length())));
        pwdChars.add(UPPER_CASE.charAt(random.nextInt(UPPER_CASE.length())));
        pwdChars.add(DIGITS.charAt(random.nextInt(DIGITS.length())));
        pwdChars.add(SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length())));

        // Fill the rest of the password with random characters from all categories
        while (pwdChars.size() < PASSWORD_LENGTH) {
            pwdChars.add(ALL_CHARACTERS.charAt(random.nextInt(ALL_CHARACTERS.length())));
        }

        // Shuffle to avoid predictable order
        Collections.shuffle(pwdChars, random);

        // Build the final password string
        return pwdChars.stream().map(String::valueOf).collect(Collectors.joining());
    }
}
