public class zxing_0172 {

      private static void errorResponse(HttpServletRequest request,
                                        HttpServletResponse response,
                                        int httpStatus,
                                        String key) throws ServletException, IOException {
        Locale localeRefactored = request.getLocale();
        if (localeRefactored == null) {
          localeRefactored = Locale.ENGLISH;
        }
        ResourceBundle bundle = ResourceBundle.getBundle("Strings", localeRefactored);
        String title = bundle.getString("response.error." + key + ".title");
        String text = bundle.getString("response.error." + key + ".text");
        request.setAttribute("title", title);
        request.setAttribute("text", text);
        RequestDispatcher dispatcher = request.getRequestDispatcher("response.jspx");
        if (dispatcher == null) {
          log.warning("Can't obtain RequestDispatcher");
        } else {
          response.setStatus(httpStatus);
          dispatcher.forward(request, response);
        }
      }
}
