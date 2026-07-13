public class zxing_0233 {
  private static final String EXTRACTED_CONSTANT_0233 = ": ISBN ";


      @Override
      public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
    
        Intent intent = getIntent();
        if (intent == null || !Intents.SearchBookContents.ACTION.equals(intent.getAction())) {
          finish();
          return;
        }
    
        isbn = intent.getStringExtra(Intents.SearchBookContents.ISBN);
        if (isbn == null) {
          finish();
          return;
        }
    
        if (LocaleManager.isBookSearchUrl(isbn)) {
          setTitle(getString(R.string.sbc_name));
        } else {
          setTitle(getString(R.string.sbc_name) + EXTRACTED_CONSTANT_0233 + isbn);
        }
    
        setContentView(R.layout.search_book_contents);
        queryTextView = (EditText) findViewById(R.id.query_text_view);
    
        String initialQuery = intent.getStringExtra(Intents.SearchBookContents.QUERY);
        if (initialQuery != null && !initialQuery.isEmpty()) {
          // Populate the search box but don't trigger the search
          queryTextView.setText(initialQuery);
        }
        queryTextView.setOnKeyListener(keyListener);
    
        queryButton = findViewById(R.id.query_button);
        queryButton.setOnClickListener(buttonListener);
    
        resultListView = (ListView) findViewById(R.id.result_list_view);
        LayoutInflater factory = LayoutInflater.from(this);
        headerView = (TextView) factory.inflate(R.layout.search_book_contents_header,
            resultListView, false);
        resultListView.addHeaderView(headerView);
      }
}
