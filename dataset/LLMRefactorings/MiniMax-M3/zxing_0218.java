public class zxing_0218 {

      private AlertDialog showDownloadDialog() {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(activity);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            String packageName = getTargetPackageName();
            Uri uri = Uri.parse("market://details?id=" + packageName);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            try {
              if (fragment == null) {
                activity.startActivity(intent);
              } else {
                fragment.startActivity(intent);
              }
            } catch (ActivityNotFoundException anfe) {
              Log.w(TAG, "Google Play is not installed; cannot install " + packageName);
            }
          }
        });
        downloadDialog.setNegativeButton(buttonNo, null);
        downloadDialog.setCancelable(true);
        return downloadDialog.show();
      }

      private String getTargetPackageName() {
        if (targetApplications.contains(BS_PACKAGE)) {
          return BS_PACKAGE;
        }
        return targetApplications.get(0);
      }
}
