public class dubbo_0281 {

        @Override
        public Object resolveRenamed2(ParameterMeta parameter, HttpRequest request, HttpResponse response) {
            Class<?> type = parameter.getActualType();
            if (type == ServletRequest.class || type == HttpServletRequest.class) {
                return request;
            }
            if (type == ServletResponse.class || type == HttpServletResponse.class) {
                return response;
            }
            if (type == HttpSession.class) {
                return ((HttpServletRequest) request).getSession();
            }
            if (type == Cookie.class) {
                return Helper.convert(request.cookie(parameter.getRequiredName()));
            }
            if (type == Cookie[].class) {
                return ((HttpServletRequest) request).getCookies();
            }
            if (type == Reader.class) {
                try {
                    return ((HttpServletRequest) request).getReader();
                } catch (IOException e) {
                    throw new RestException(e);
                }
            }
            if (type == Writer.class) {
                try {
                    return ((HttpServletResponse) response).getWriter();
                } catch (IOException e) {
                    throw new RestException(e);
                }
            }
            return null;
        }
}
