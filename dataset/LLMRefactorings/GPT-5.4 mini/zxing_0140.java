public class zxing_0140 {
  private static final String EXTRACTED_CONSTANT_0140 = "BOARD";


      public static String collectStats(CharSequence flattenedParams) {
        StringBuilder result = new StringBuilder(1000);
        appendStat(result, EXTRACTED_CONSTANT_0140, Build.BOARD);
        appendStat(result, "BRAND", Build.BRAND);
        appendStat(result, "CPU_ABI", Build.CPU_ABI);
        appendStat(result, "DEVICE", Build.DEVICE);
        appendStat(result, "DISPLAY", Build.DISPLAY);
        appendStat(result, "FINGERPRINT", Build.FINGERPRINT);
        appendStat(result, "HOST", Build.HOST);
        appendStat(result, "ID", Build.ID);
        appendStat(result, "MANUFACTURER", Build.MANUFACTURER);
        appendStat(result, "MODEL", Build.MODEL);
        appendStat(result, "PRODUCT", Build.PRODUCT);
        appendStat(result, "TAGS", Build.TAGS);
        appendStat(result, "TIME", Build.TIME);
        appendStat(result, "TYPE", Build.TYPE);
        appendStat(result, "USER", Build.USER);
        appendStat(result, "VERSION.CODENAME", Build.VERSION.CODENAME);
        appendStat(result, "VERSION.INCREMENTAL", Build.VERSION.INCREMENTAL);
        appendStat(result, "VERSION.RELEASE", Build.VERSION.RELEASE);
        appendStat(result, "VERSION.SDK_INT", Build.VERSION.SDK_INT);
    
        if (flattenedParams != null) {
          String[] params = SEMICOLON.split(flattenedParams);
          Arrays.sort(params);
          for (String param : params) {
            result.append(param).append('\n');
          }
        }
    
        return result.toString();
      }
}
