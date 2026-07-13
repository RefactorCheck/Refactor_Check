public class zxing_0006 {

        private void handleSearchResults(JSONObject json) {
          try {
            int count = json.getInt("number_of_results");
            headerView.setText(getString(R.string.msg_sbc_results) + " : " + count);
            if (count > 0) {
              JSONArray results = json.getJSONArray("search_results");
              SearchBookContentsResult.setQuery(queryTextView.getText().toString());
              List<SearchBookContentsResult> items = new ArrayList<>(count);
              for (int x = 0; x < count; x++) {
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
