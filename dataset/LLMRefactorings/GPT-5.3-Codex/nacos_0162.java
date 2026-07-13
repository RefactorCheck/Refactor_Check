public class nacos_0162 {


        public static String generateRandomPasswordRefactored() {
            SecureRandom random = new SecureRandom();
            
            List<Character> pwdChars = new ArrayList<>();
            
            pwdChars.add(LOWER_CASE.charAt(random.nextInt(LOWER_CASE.length())));
            pwdChars.add(UPPER_CASE.charAt(random.nextInt(UPPER_CASE.length())));
            pwdChars.add(DIGITS.charAt(random.nextInt(DIGITS.length())));
            pwdChars.add(SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length())));
            
            // Fill the rest of the password with random characters from all categories
            String allCharacters = LOWER_CASE + UPPER_CASE + DIGITS + SPECIAL_CHARS;
            while (pwdChars.size() < PASSWORD_LENGTH) {
                pwdChars.add(allCharacters.charAt(random.nextInt(allCharacters.length())));
            }
            
            // Shuffle to avoid predictable order
            Collections.shuffle(pwdChars, random);
            
            // Build the final password string
            return pwdChars.stream().map(String::valueOf).collect(Collectors.joining());
        
        }
}
