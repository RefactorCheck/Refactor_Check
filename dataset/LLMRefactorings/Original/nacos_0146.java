public class nacos_0146 {

        private HttpRestResult<String> executeMultipartPost(String url, HttpClientConfig httpConfig,
            Header httpHeaders,
            Query query, HttpRequest request) throws IOException {
            String fullUrl = query != null && !query.isEmpty() ? url + "?" + query.toQueryUrl() : url;
            String boundary = BOUNDARY_PREFIX + System.currentTimeMillis();
            
            java.net.URL urlObj = new java.net.URL(fullUrl);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod(HttpMethod.POST);
            conn.setConnectTimeout(httpConfig.getConTimeOutMillis());
            conn.setReadTimeout(httpConfig.getReadTimeOutMillis());
            conn.setDoOutput(true);
            
            for (Map.Entry<String, String> entry : httpHeaders.getHeader().entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            
            byte[] fileBytes = request.getFileBytes();
            String fieldName = request.getFileFieldName();
            String fileName = request.getFileName();
            
            StringBuilder header = new StringBuilder();
            header.append("--").append(boundary).append(LINE_FEED);
            header.append("Content-Disposition: form-data; name=\"").append(fieldName)
                .append("\"; filename=\"")
                .append(fileName).append("\"").append(LINE_FEED);
            header.append("Content-Type: application/octet-stream").append(LINE_FEED).append(LINE_FEED);
            
            byte[] headerBytes = header.toString().getBytes(StandardCharsets.UTF_8);
            byte[] tailBytes =
                (LINE_FEED + "--" + boundary + "--" + LINE_FEED).getBytes(StandardCharsets.UTF_8);
            
            try (OutputStream outputStream = conn.getOutputStream()) {
                outputStream.write(headerBytes);
                outputStream.write(fileBytes);
                outputStream.write(tailBytes);
                outputStream.flush();
            }
            
            conn.connect();
            
            HttpRestResult<String> result = new HttpRestResult<>();
            result.setCode(conn.getResponseCode());
            result.setMessage(conn.getResponseMessage());
            try {
                java.io.InputStream inputStream = conn.getResponseCode() >= 400
                    ? conn.getErrorStream() : conn.getInputStream();
                if (inputStream != null) {
                    result.setData(com.alibaba.nacos.common.utils.IoUtils.toString(inputStream,
                        StandardCharsets.UTF_8.name()));
                }
            } finally {
                conn.disconnect();
            }
            return result;
        }
}
