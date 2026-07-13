public class nacos_0069 {

        public Result<Map<String, Object>> importConfig(String sourceUser, String namespaceId,
            SameConfigPolicy policy,
            MultipartFile importFile, String sourceIp, String sourceApp) throws NacosException {
            String serverContextPath = remoteServerConnector.getServerContextPath();
            Member serverMember = remoteServerConnector.randomOneHealthyMember();
            String url =
                String.format(REMOTE_CONFIG_IMPORT_URL, serverMember.getAddress(), serverContextPath);
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                Query query = Query.newInstance().addParam("namespaceId", namespaceId)
                    .addParam("srcUser", sourceUser);
                URI uri = HttpUtils.buildUri(url, query);
                HttpPost httpPost = new HttpPost(uri);
                httpPost.setHeader(WebUtils.X_FORWARDED_FOR, sourceIp);
                httpPost.setHeader(RequestUtil.CLIENT_APPNAME_HEADER, sourceApp);
                remoteServerConnector.addAuthIdentity(httpPost);
                HttpEntity entity = buildImportConfigEntity(importFile, policy);
                httpPost.setEntity(entity);
                String executeResult =
                    httpClient.execute(httpPost, new BasicHttpClientResponseHandler());
                return JacksonUtils.toObj(executeResult, new TypeReference<>() {
                });
            } catch (HttpResponseException responseException) {
                LOGGER.error("Import config to server {} failed with code {}: ",
                    serverMember.getAddress(),
                    responseException.getStatusCode());
                throw new NacosRuntimeException(responseException.getStatusCode(),
                    responseException.getMessage());
            } catch (IOException | URISyntaxException e) {
                LOGGER.error("Import config to server {} failed: ", serverMember.getAddress(), e);
                throw new NacosRuntimeException(NacosException.SERVER_ERROR,
                    "Import config to server failed.");
            }
        }

        private HttpEntity buildImportConfigEntity(MultipartFile importFile, SameConfigPolicy policy) throws IOException {
            String contentTypeString =
                null == importFile.getContentType() ? MediaType.MULTIPART_FORM_DATA_VALUE
                    : importFile.getContentType();
            ContentType contentType = ContentType.create(contentTypeString, Constants.ENCODE);
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            multipartEntityBuilder.addBinaryBody("file", importFile.getInputStream(), contentType,
                importFile.getOriginalFilename());
            multipartEntityBuilder.addTextBody("policy", policy.name(), contentType);
            return multipartEntityBuilder.build();
        }
}
