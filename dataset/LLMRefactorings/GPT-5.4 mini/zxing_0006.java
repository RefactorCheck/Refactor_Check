public class zxing_0006 {

        private void handleSearchResults(JSONObject json) {
          try {
            int countRefactored = json.getInt("number_of_results");
            headerView.setText(getString(R.string.msg_sbc_results) + " : " + countRefactored);
            if (countRefactored > 0) {
              JSONArray results = json.getJSONArray("search_results");
              SearchBookContentsResult.setQuery(queryTextView.getText().toString());
              List<SearchBookContentsResult> items = new ArrayList<>(countRefactored);
              for (int x = 0; x < countRefactored; x++) {
                items.add(parseResult(results.getJSONObject(x)));
              }
              resultListView.setOnItemClickListener(new BrowseBookListener(SearchBookContentsActivity.this, items));
              resultListView.setAdapter(new SearchBookContentsAdapter(SearchBookContentsActivity.this, items));
            } else {
              String searchable = json.optString("searchable");
              if ("false".equals(searchable)) {
                headerView.setText(R.string.msg_sbc_book_not_searchable);
              }
              resultListView.setAdapter(null);
            }
          } catch (JSONException e) {
            Log.w(TAG, "Bad JSON from book search", e);
            resultListView.setAdapter(null);
            headerView.setText(R.string.msg_sbc_failed);
          }
        }
}
