public class zxing_0218 {
  private static final String EXTRACTED_CONSTANT_0218 = "market://details?id=";


      private AlertDialog showDownloadDialog() {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(activity);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            String packageName;
            if (targetApplications.contains(BS_PACKAGE)) {
              // Prefer to suggest download of BS if it's anywhere in the list
              packageName = BS_PACKAGE;
            } else {
              // Otherwise, first option:
              packageName = targetApplications.get(0);
            }
            Uri uri = Uri.parse(EXTRACTED_CONSTANT_0218 + packageName);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            try {
              if (fragment == null) {
                activity.startActivity(intent);
              } else {
                fragment.startActivity(intent);
              }
            } catch (ActivityNotFoundException anfe) {
              // Hmm, market is not installed
              Log.w(TAG, "Google Play is not installed; cannot install " + packageName);
            }
          }
        });
        downloadDialog.setNegativeButton(buttonNo, null);
        downloadDialog.setCancelable(true);
        return downloadDialog.show();
      }
}
