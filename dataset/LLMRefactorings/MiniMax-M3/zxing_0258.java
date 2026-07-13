public class zxing_0258 {

        private SearchBookContentsResult parseResult(JSONObject json) {
    
          String pageId;
          String pageNumber;
          String snippet;
          try {
            pageId = json.getString("page_id");
            pageNumber = json.optString("page_number");
            snippet = json.optString("snippet_text");        
          } catch (JSONException e) {
            Log.w(TAG, e);
            return new SearchBookContentsResult(getString(R.string.msg_sbc_no_page_returned), "", "", false);
          }
          
          if (pageNumber == null || pageNumber.isEmpty()) {
            pageNumber = "";
          } else {
            pageNumber = getString(R.string.msg_sbc_page) + ' ' + pageNumber;
          }
          
          boolean valid = snippet != null && !snippet.isEmpty();
          if (valid) {
            snippet = cleanSnippet(snippet);
          } else {
            snippet = '(' + getString(R.string.msg_sbc_snippet_unavailable) + ')';        
          }
    
          return new SearchBookContentsResult(pageId, pageNumber, snippet, valid);
        }
    
        private String cleanSnippet(String snippet) {
          snippet = TAG_PATTERN.matcher(snippet).replaceAll("");
          snippet = LT_ENTITY_PATTERN.matcher(snippet).replaceAll("<");
          snippet = GT_ENTITY_PATTERN.matcher(snippet).replaceAll(">");
          snippet = QUOTE_ENTITY_PATTERN.matcher(snippet).replaceAll("'");
          snippet = QUOT_ENTITY_PATTERN.matcher(snippet).replaceAll("\"");
          return snippet;
        }
}
