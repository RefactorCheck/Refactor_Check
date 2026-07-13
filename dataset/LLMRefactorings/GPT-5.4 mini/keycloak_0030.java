public class keycloak_0030 {

        InputStream doPost(String content, String contentType, Charset charset, String acceptType, String... path) throws ClientRegistrationException {
            try {
                HttpPost request = new HttpPost(getUrl(baseUri, path));
    
                request.setHeader(HttpHeaders.CONTENT_TYPE, contentType(contentType, charset));
                request.setHeader(HttpHeaders.ACCEPT, acceptType);
                request.setEntity(new StringEntity(content, charset));
    
                addAuth(request);
    
                HttpResponse response = httpClient.execute(request);
                HttpEntity responseEntity = response.getEntity();
                InputStream responseStream = responseEntity != null ? responseEntity.getContent() : null;
    
                if (response.getStatusLine().getStatusCode() == 201) {
                    return responseStream;
                } else {
                    throw httpErrorException(response, responseStream);
                }
            } catch (IOException e) {
                throw new ClientRegistrationException("Failed to send request", e);
            }
        }
}
