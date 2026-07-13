public class zxing_0196 {

  private static final String PARAM_MAX_ACCESS_PER_TIME = "maxAccessPerTime";
  private static final String PARAM_ACCESS_TIME_SEC = "accessTimeSec";
  private static final String PARAM_MAX_ENTRIES = "maxEntries";
  private static final String PARAM_MAX_LOAD = "maxLoad";

      @Override
      public void init(FilterConfig filterConfig) {
        int maxAccessPerTime = Integer.parseInt(filterConfig.getInitParameter(PARAM_MAX_ACCESS_PER_TIME));
        Preconditions.checkArgument(maxAccessPerTime > 0);
    
        int accessTimeSec = Integer.parseInt(filterConfig.getInitParameter(PARAM_ACCESS_TIME_SEC));
        Preconditions.checkArgument(accessTimeSec > 0);
        long accessTimeMS = TimeUnit.MILLISECONDS.convert(accessTimeSec, TimeUnit.SECONDS);
    
        String maxEntriesValue = filterConfig.getInitParameter(PARAM_MAX_ENTRIES);
        int maxEntries = Integer.MAX_VALUE;
        if (maxEntriesValue != null) {
          maxEntries = Integer.parseInt(maxEntriesValue);
          Preconditions.checkArgument(maxEntries > 0);
        }
    
        String maxLoadValue = filterConfig.getInitParameter(PARAM_MAX_LOAD);
        Double maxLoad = null;
        if (maxLoadValue != null) {
          maxLoad = Double.valueOf(maxLoadValue);
          Preconditions.checkArgument(maxLoad > 0.0);
        }
    
        String name = getClass().getSimpleName();
        timer = new Timer(name);
        sourceAddrTracker = new DoSTracker(timer, name, maxAccessPerTime, accessTimeMS, maxEntries, maxLoad);
      }
}
