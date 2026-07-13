public class arthas_0049 {

        private HttpResponse readFileFromResource(FullHttpRequest request, String path) throws IOException {
            DefaultFullHttpResponse fullResp = null;
            InputStream in = null;
            try {
                URL res = HttpTtyConnection.class.getResource("/com/taobao/arthas/core/http" + path);
                if (res != null) {
                    fullResp = new DefaultFullHttpResponse(request.protocolVersion(),
                            HttpResponseStatus.OK);
                    in = res.openStream();
                    byte[] tmp = new byte[256];
                    for (int l = 0; l != -1; l = in.read(tmp)) {
                        fullResp.content().writeBytes(tmp, 0, l);
                    }
                    String contentType = getContentType(path);
                    if (contentType != null) {
                        fullResp.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType);
                    }
                }
            } finally {
                IOUtils.close(in);
            }
            return fullResp;
        }

        private String getContentType(String path) {
            int li = path.lastIndexOf('.');
            if (li != -1 && li != path.length() - 1) {
                String ext = path.substring(li + 1);
                if ("html".equals(ext)) {
                    return "text/html";
                } else if ("js".equals(ext)) {
                    return "application/javascript";
                } else if ("css".equals(ext)) {
                    return "text/css";
                }
            }
            return null;
        }
}
