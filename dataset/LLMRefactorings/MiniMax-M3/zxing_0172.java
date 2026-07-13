public class zxing_0172 {

      private static final String BUNDLE_NAME = "Strings";
      private static final String ERROR_PAGE = "response.jspx";
      private static final String TITLE_ATTR = "title";
      private static final String TEXT_ATTR = "text";

      private static void errorResponse(HttpServletRequest request,
                                        HttpServletResponse response,
                                        int httpStatus,
                                        String key) throws ServletException, IOException {
        Locale locale = request.getLocale();
        if (locale == null) {
          locale = Locale.ENGLISH;
        }
        ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
        String title = bundle.getString("response.error." + key + ".title");
        String text = bundle.getString("response.error." + key + ".text");
        request.setAttribute(TITLE_ATTR, title);
        request.setAttribute(TEXT_ATTR, text);
        RequestDispatcher dispatcher = request.getRequestDispatcher(ERROR_PAGE);
        if (dispatcher == null) {
          log.warning("Can't obtain RequestDispatcher");
        } else {
          response.setStatus(httpStatus);
          dispatcher.forward(request, response);
        }
      }
}
