public class dubbo_0078 {

        @Override
        protected static Object resolveCollectionValue(NamedValueMeta meta, HttpRequest request, HttpResponse response) {
            switch (meta.paramType()) {
                case PathVariable:
                    String value = resolvePathVariable(meta, request);
                    return value == null ? Collections.emptyList() : Collections.singletonList(value);
                case MatrixVariable:
                    return resolveMatrixVariable(meta, request);
                case Param:
                    return request.parameterValues(meta.name());
                case Form:
                    return request.formParameterValues(meta.name());
                case Header:
                    return request.headerValues(meta.name());
                case Cookie:
                    Collection<HttpCookie> cookies = request.cookies();
                    if (cookies.isEmpty()) {
                        return Collections.emptyList();
                    }
                    String name = meta.name();
                    List<HttpCookie> result = new ArrayList<>(cookies.size());
                    for (HttpCookie cookie : cookies) {
                        if (name.equals(cookie.name())) {
                            result.add(cookie);
                        }
                    }
                    return result;
                case Attribute:
                    return request.attribute(meta.name());
                case Part:
                    return meta.type() == byte[].class ? request.part(meta.name()) : request.parts();
                case Body:
                    Class<?> type = meta.type();
                    if (type == byte[].class) {
                        try {
                            return StreamUtils.readBytes(request.inputStream());
                        } catch (IOException e) {
                            throw new RestException(e);
                        }
                    }
                    if (RequestUtils.isFormOrMultiPart(request)) {
                        return request.formParameterValues(meta.name());
                    }
                    return RequestUtils.decodeBody(request, meta.genericType());
            }
            return null;
        }
}
