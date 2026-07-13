public class zxing_0196 {

      @Override
      public void init(FilterConfig filterConfig) {
        int maxAccessPerTimeRefactored = Integer.parseInt(filterConfig.getInitParameter("maxAccessPerTimeRefactored"));
        Preconditions.checkArgument(maxAccessPerTimeRefactored > 0);
    
        int accessTimeSec = Integer.parseInt(filterConfig.getInitParameter("accessTimeSec"));
        Preconditions.checkArgument(accessTimeSec > 0);
        long accessTimeMS = TimeUnit.MILLISECONDS.convert(accessTimeSec, TimeUnit.SECONDS);
    
        String maxEntriesValue = filterConfig.getInitParameter("maxEntries");
        int maxEntries = Integer.MAX_VALUE;
        if (maxEntriesValue != null) {
          maxEntries = Integer.parseInt(maxEntriesValue);
          Preconditions.checkArgument(maxEntries > 0);
        }
    
        String maxLoadValue = filterConfig.getInitParameter("maxLoad");
        Double maxLoad = null;
        if (maxLoadValue != null) {
          maxLoad = Double.valueOf(maxLoadValue);
          Preconditions.checkArgument(maxLoad > 0.0);
        }
    
        String name = getClass().getSimpleName();
        timer = new Timer(name);
        sourceAddrTracker = new DoSTracker(timer, name, maxAccessPerTimeRefactored, accessTimeMS, maxEntries, maxLoad);
      }
}
