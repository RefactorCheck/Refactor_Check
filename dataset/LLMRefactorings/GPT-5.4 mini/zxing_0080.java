public class zxing_0080 {
  private static final String EXTRACTED_CONSTANT_0080 = ".ENCODE";


      public final AlertDialog shareText(CharSequence text, CharSequence type) {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setAction(BS_PACKAGE + EXTRACTED_CONSTANT_0080);
        intent.putExtra("ENCODE_TYPE", type);
        intent.putExtra("ENCODE_DATA", text);
        String targetAppPackage = findTargetAppPackage(intent);
        if (targetAppPackage == null) {
          return showDownloadDialog();
        }
        intent.setPackage(targetAppPackage);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(FLAG_NEW_DOC);
        attachMoreExtras(intent);
        if (fragment == null) {
          activity.startActivity(intent);
        } else {
          fragment.startActivity(intent);
        }
        return null;
      }
}
