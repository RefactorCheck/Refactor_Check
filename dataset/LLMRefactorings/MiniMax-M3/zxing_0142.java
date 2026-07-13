public class zxing_0142 {

      private static final String LOGGER_NAME = "com.google.zxing";
      private static final String BLOCKED_URLS_RESOURCE = "/private/uri-block-substrings.txt";
      private static final String PARAM_MAX_ACCESS_PER_TIME = "maxAccessPerTime";
      private static final String PARAM_ACCESS_TIME_SEC = "accessTimeSec";
      private static final String PARAM_MAX_ENTRIES = "maxEntries";

      @Override
      public void init(ServletConfig servletConfig) throws ServletException {
        Logger logger = Logger.getLogger(LOGGER_NAME);
        ServletContext context = servletConfig.getServletContext();
        logger.addHandler(new ServletContextLogHandler(context));
    
        URL blockURL = context.getClassLoader().getResource(BLOCKED_URLS_RESOURCE);
        if (blockURL == null) {
          blockedURLSubstrings = Collections.emptyList();
        } else {
          try {
            blockedURLSubstrings = Resources.readLines(blockURL, StandardCharsets.UTF_8);
          } catch (IOException ioe) {
            throw new ServletException(ioe);
          }
          log.info("Blocking URIs containing: " + blockedURLSubstrings);
        }
    
        int maxAccessPerTime = Integer.parseInt(servletConfig.getInitParameter(PARAM_MAX_ACCESS_PER_TIME));
        int accessTimeSec = Integer.parseInt(servletConfig.getInitParameter(PARAM_ACCESS_TIME_SEC));
        long accessTimeMS = TimeUnit.MILLISECONDS.convert(accessTimeSec, TimeUnit.SECONDS);
        int maxEntries = Integer.parseInt(servletConfig.getInitParameter(PARAM_MAX_ENTRIES));
    
        String name = getClass().getSimpleName();
        timer = new Timer(name);
        destHostTracker = new DoSTracker(timer, name, maxAccessPerTime, accessTimeMS, maxEntries, null);
      }
}
