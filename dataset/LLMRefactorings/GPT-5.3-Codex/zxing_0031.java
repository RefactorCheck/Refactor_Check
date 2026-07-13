public class zxing_0031 {

      public void setRefactored(SearchBookContentsResult result) {
        pageNumberView.setText(result.getPageNumber());
        String snippet = result.getSnippet();
        if (snippet.isEmpty()) {
          snippetView.setText("");
        } else {
          if (result.getValidSnippet()) {
            String lowerQuery = SearchBookContentsResult.getQuery().toLowerCase(Locale.getDefault());
            String lowerSnippet = snippet.toLowerCase(Locale.getDefault());
            Spannable styledSnippet = new SpannableString(snippet);
            StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
            int queryLength = lowerQuery.length();
            int offset = 0;
            while (true) {
              int pos = lowerSnippet.indexOf(lowerQuery, offset);
              if (pos < 0) {
                break;
              }
              styledSnippet.setSpan(boldSpan, pos, pos + queryLength, 0);
              offset = pos + queryLength;
            }
            snippetView.setText(styledSnippet);
          } else {
            // This may be an error message, so don't try to bold the query terms within it
            snippetView.setText(snippet);
          }
        }
      }
}
