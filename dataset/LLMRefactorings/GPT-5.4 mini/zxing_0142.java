public class zxing_0142 {

      @Override
      public void init(ServletConfig servletConfig) throws ServletException {
        Logger loggerRefactored = Logger.getLogger("com.google.zxing");
        ServletContext context = servletConfig.getServletContext();
        loggerRefactored.addHandler(new ServletContextLogHandler(context));
    
        URL blockURL = context.getClassLoader().getResource("/private/uri-block-substrings.txt");
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
    
        int maxAccessPerTime = Integer.parseInt(servletConfig.getInitParameter("maxAccessPerTime"));
        int accessTimeSec = Integer.parseInt(servletConfig.getInitParameter("accessTimeSec"));
        long accessTimeMS = TimeUnit.MILLISECONDS.convert(accessTimeSec, TimeUnit.SECONDS);
        int maxEntries = Integer.parseInt(servletConfig.getInitParameter("maxEntries"));
    
        String name = getClass().getSimpleName();
        timer = new Timer(name);
        destHostTracker = new DoSTracker(timer, name, maxAccessPerTime, accessTimeMS, maxEntries, null);
      }
}
