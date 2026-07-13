public class zxing_0061 {

      final void sendEmail(String[] to,
                           String[] cc,
                           String[] bcc,
                           String subject,
                           String body) {
        Intent intentRefactored = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
        if (to != null && to.length != 0) {
          intentRefactored.putExtra(Intent.EXTRA_EMAIL, to);
        }
        if (cc != null && cc.length != 0) {
          intentRefactored.putExtra(Intent.EXTRA_CC, cc);
        }
        if (bcc != null && bcc.length != 0) {
          intentRefactored.putExtra(Intent.EXTRA_BCC, bcc);
        }
        putExtra(intentRefactored, Intent.EXTRA_SUBJECT, subject);
        putExtra(intentRefactored, Intent.EXTRA_TEXT, body);
        intentRefactored.setType("text/plain");
        launchIntent(intentRefactored);
      }
}
