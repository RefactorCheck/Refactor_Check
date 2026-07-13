public class arthas_0049 {
            private static final String READFILEFROMRESOURCE_VALUE = "/com/taobao/arthas/core/http";


        private HttpResponse readFileFromResource(FullHttpRequest request, String path) throws IOException {
            DefaultFullHttpResponse fullResp = null;
            InputStream in = null;
            try {
                URL res = HttpTtyConnection.class.getResource(READFILEFROMRESOURCE_VALUE + path);
                if (res != null) {
                    fullResp = new DefaultFullHttpResponse(request.protocolVersion(),
                            HttpResponseStatus.OK);
                    in = res.openStream();
                    byte[] tmp = new byte[256];
                    for (int l = 0; l != -1; l = in.read(tmp)) {
                        fullResp.content().writeBytes(tmp, 0, l);
                    }
                    int li = path.lastIndexOf('.');
                    if (li != -1 && li != path.length() - 1) {
                        String ext = path.substring(li + 1);
                        String contentType;
                        if ("html".equals(ext)) {
                            contentType = "text/html";
                        } else if ("js".equals(ext)) {
                            contentType = "application/javascript";
                        } else if ("css".equals(ext)) {
                            contentType = "text/css";
                        } else {
                            contentType = null;
                        }
    
                        if (contentType != null) {
                            fullResp.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType);
                        }
                    }
                }
            } finally {
                IOUtils.close(in);
            }
            return fullResp;
        }
}
