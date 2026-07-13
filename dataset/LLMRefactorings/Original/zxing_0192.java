public class zxing_0192 {

      public static void maybeInvokeRetrieval(TextView textView,
                                              ParsedResult result,
                                              HistoryManager historyManager,
                                              Context context) {
        try {
          if (result instanceof URIParsedResult) {
            SupplementalInfoRetriever uriRetriever =
                new URIResultInfoRetriever(textView, (URIParsedResult) result, historyManager, context);
            uriRetriever.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            SupplementalInfoRetriever titleRetriever =
                new TitleRetriever(textView, (URIParsedResult) result, historyManager);
            titleRetriever.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
          } else if (result instanceof ProductParsedResult) {
            ProductParsedResult productParsedResult = (ProductParsedResult) result;
            String productID = productParsedResult.getProductID();
            SupplementalInfoRetriever productRetriever =
                new ProductResultInfoRetriever(textView, productID, historyManager, context);
            productRetriever.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
          } else if (result instanceof ISBNParsedResult) {
            String isbn = ((ISBNParsedResult) result).getISBN();
            SupplementalInfoRetriever productInfoRetriever =
                new ProductResultInfoRetriever(textView, isbn, historyManager, context);
            productInfoRetriever.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            SupplementalInfoRetriever bookInfoRetriever =
                new BookResultInfoRetriever(textView, isbn, historyManager, context);
            bookInfoRetriever.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
          }
        } catch (RejectedExecutionException ree) {
          // do nothing
        }
      }
}
