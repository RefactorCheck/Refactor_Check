public class zxing_0031 {
      public void set(SearchBookContentsResult result) {
        pageNumberView.setText(result.getPageNumber());
        String snippet = result.getSnippet();
        if (snippet.isEmpty()) {
          snippetView.setText("");
        } else {
          if (result.getValidSnippet()) {
            snippetView.setText(boldQueryTerms(snippet, SearchBookContentsResult.getQuery()));
          } else {
            // This may be an error message, so don't try to bold the query terms within it
            snippetView.setText(snippet);
          }
        }
      }

      private Spannable boldQueryTerms(String snippet, String query) {
        String lowerQuery = query.toLowerCase(Locale.getDefault());
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
        return styledSnippet;
      }
}
