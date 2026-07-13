public class zxing_0061 {

      final void sendEmail(String[] to,
                           String[] cc,
                           String[] bcc,
                           String subject,
                           String body, boolean normalize) {
        Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
        if (to != null && to.length != 0) {
          intent.putExtra(Intent.EXTRA_EMAIL, to);
        }
        if (cc != null && cc.length != 0) {
          intent.putExtra(Intent.EXTRA_CC, cc);
        }
        if (bcc != null && bcc.length != 0) {
          intent.putExtra(Intent.EXTRA_BCC, bcc);
        }
        putExtra(intent, Intent.EXTRA_SUBJECT, subject);
        putExtra(intent, Intent.EXTRA_TEXT, body);
        intent.setType("text/plain");
        launchIntent(intent);
      }
}
