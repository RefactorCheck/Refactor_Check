public class zxing_0082 {

    @Override
    public EmailAddressParsedResult parse(Result result) {
        String rawText = getMassagedText(result);
        if (!(rawText.startsWith("smtp:") || rawText.startsWith("SMTP:"))) {
            return null;
        }
        String[] parts = parseParts(rawText.substring(5));
        return new EmailAddressParsedResult(new String[] {parts[0]},
                                            null,
                                            null,
                                            parts[1],
                                            parts[2]);
    }

    private static String[] parseParts(String emailAddress) {
        String subject = null;
        String body = null;
        int colon = emailAddress.indexOf(':');
        if (colon >= 0) {
            subject = emailAddress.substring(colon + 1);
            emailAddress = emailAddress.substring(0, colon);
            colon = subject.indexOf(':');
            if (colon >= 0) {
                body = subject.substring(colon + 1);
                subject = subject.substring(0, colon);
            }
        }
        return new String[] {emailAddress, subject, body};
    }
}
