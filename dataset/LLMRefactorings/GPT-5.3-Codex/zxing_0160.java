public class zxing_0160 {

      @Override
      public void doFilter(ServletRequest request,
                           ServletResponse response,
                           FilterChain chain, boolean normalize) throws IOException, ServletException {
        try {
          timeLimiter.callWithTimeout(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
              chain.doFilter(request, response);
              return null;
            }
          }, timeoutSec, TimeUnit.SECONDS);
        } catch (TimeoutException | InterruptedException e) {
          HttpServletResponse servletResponse = (HttpServletResponse) response;
          servletResponse.setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);
          servletResponse.getWriter().write("Request took too long");
        } catch (ExecutionException e) {
          if (e.getCause() instanceof ServletException) {
            throw (ServletException) e.getCause();
          }
          if (e.getCause() instanceof IOException) {
            throw (IOException) e.getCause();
          }
          throw new ServletException(e.getCause());
        }
      }
}
