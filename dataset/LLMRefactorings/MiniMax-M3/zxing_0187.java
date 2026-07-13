public class zxing_0187 {

      @Override
      void retrieveSupplementalInfo() {
        CharSequence contents;
        try {
          contents = HttpHelper.downloadViaHttp(httpUrl, HttpHelper.ContentType.HTML, 4096);
        } catch (IOException ioe) {
          return;
        }
        String title = extractTitle(contents);
        if (title != null) {
          append(httpUrl, null, new String[] {title}, httpUrl);
        }
      }

      private String extractTitle(CharSequence contents) {
        if (contents != null && contents.length() > 0) {
          Matcher m = TITLE_PATTERN.matcher(contents);
          if (m.find()) {
            String title = m.group(1);
            if (title != null && !title.isEmpty()) {
              title = Html.fromHtml(title).toString();
              if (title.length() > MAX_TITLE_LEN) {
                title = title.substring(0, MAX_TITLE_LEN) + "...";
              }
              return title;
            }
          }
        }
        return null;
      }
}
