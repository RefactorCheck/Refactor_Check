public class dubbo_0281 {

        @Override
        public Object resolve(ParameterMeta parameter, HttpRequest request, HttpResponse response) {
            Class<?> type = parameter.getActualType();
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            if (type == ServletRequest.class || type == HttpServletRequest.class) {
                return request;
            }
            if (type == ServletResponse.class || type == HttpServletResponse.class) {
                return response;
            }
            if (type == HttpSession.class) {
                return httpRequest.getSession();
            }
            if (type == Cookie.class) {
                return Helper.convert(request.cookie(parameter.getRequiredName()));
            }
            if (type == Cookie[].class) {
                return httpRequest.getCookies();
            }
            if (type == Reader.class) {
                try {
                    return httpRequest.getReader();
                } catch (IOException e) {
                    throw new RestException(e);
                }
            }
            if (type == Writer.class) {
                try {
                    return httpResponse.getWriter();
                } catch (IOException e) {
                    throw new RestException(e);
                }
            }
            return null;
        }
}
