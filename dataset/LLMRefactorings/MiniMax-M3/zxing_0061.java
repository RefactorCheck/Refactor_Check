public class zxing_0061 {

      final void sendEmail(String[] to,
                           String[] cc,
                           String[] bcc,
                           String subject,
                           String body) {
        Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
        addRecipients(intent, Intent.EXTRA_EMAIL, to);
        addRecipients(intent, Intent.EXTRA_CC, cc);
        addRecipients(intent, Intent.EXTRA_BCC, bcc);
        putExtra(intent, Intent.EXTRA_SUBJECT, subject);
        putExtra(intent, Intent.EXTRA_TEXT, body);
        intent.setType("text/plain");
        launchIntent(intent);
      }

      private void addRecipients(Intent intent, String key, String[] recipients) {
        if (recipients != null && recipients.length != 0) {
          intent.putExtra(key, recipients);
        }
      }
}
