public class zxing_0277 {

      public static final AlertDialog initiateScan(Collection<String> desiredBarcodeFormats, int cameraId) {
        Intent intentScan = new Intent(BS_PACKAGE + ".SCAN");
        intentScan.addCategory(Intent.CATEGORY_DEFAULT);
    
        // check which types of codes to scan for
        if (desiredBarcodeFormats != null) {
          // set the desired barcode types
          StringBuilder joinedByComma = new StringBuilder();
          for (String format : desiredBarcodeFormats) {
            if (joinedByComma.length() > 0) {
              joinedByComma.append(',');
            }
            joinedByComma.append(format);
          }
          intentScan.putExtra("SCAN_FORMATS", joinedByComma.toString());
        }
    
        // check requested camera ID
        if (cameraId >= 0) {
          intentScan.putExtra("SCAN_CAMERA_ID", cameraId);
        }
    
        String targetAppPackage = findTargetAppPackage(intentScan);
        if (targetAppPackage == null) {
          return showDownloadDialog();
        }
        intentScan.setPackage(targetAppPackage);
        intentScan.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentScan.addFlags(FLAG_NEW_DOC);
        attachMoreExtras(intentScan);
        startActivityForResult(intentScan, REQUEST_CODE);
        return null;
      }
}
