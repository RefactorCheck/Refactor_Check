public class zxing_0157 {

      @Override
      void retrieveSupplementalInfo() throws IOException {
    
        CharSequence contents = HttpHelper.downloadViaHttp("https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn,
                                                           HttpHelper.ContentType.JSON);
    
        if (contents.length() == 0) {
          return;
        }
    
        String title;
        String pages;
        Collection<String> authors = null;
    
        try {
    
          JSONObject topLevel = (JSONObject) new JSONTokener(contents.toString()).nextValue();
          JSONArray items = topLevel.optJSONArray("items");
          if (items == null || items.isNull(0)) {
            return;
          }
    
          JSONObject volumeInfo = ((JSONObject) items.get(0)).getJSONObject("volumeInfo");
          if (volumeInfo == null) {
            return;
          }
    
          title = volumeInfo.optString("title");
          pages = volumeInfo.optString("pageCount");
    
          JSONArray authorsArray = volumeInfo.optJSONArray("authors");
          if (authorsArray != null && !authorsArray.isNull(0)) {
            authors = new ArrayList<>(authorsArray.length());
            for (int i = 0; i < authorsArray.length(); i++) {
              authors.add(authorsArray.getString(i));
            }
          }
    
        } catch (JSONException e) {
          throw new IOException(e);
        }
    
        Collection<String> newTexts = new ArrayList<>();
        maybeAddText(title, newTexts);
        maybeAddTextSeries(authors, newTexts);
        maybeAddText(pages == null || pages.isEmpty() ? null : pages + "pp.", newTexts);
        
        append(isbn, source, newTexts.toArray(EMPTY_STR_ARRAY), getBaseBookUri() + isbn);
      }
      
      private String getBaseBookUri() {
        return "http://www.google." + LocaleManager.getBookSearchCountryTLD(context)
            + "/search?tbm=bks&source=zxing&q=";
      }
}
