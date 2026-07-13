public class nacos_0190 {

    private RequestResource resourceBuild(Request request) {
        if (request instanceof ConfigQueryRequest) {
            return buildConfigResource((ConfigQueryRequest) request);
        }
        if (request instanceof ConfigPublishRequest) {
            return buildConfigResource((ConfigPublishRequest) request);
        }
        if (request instanceof ConfigRemoveRequest) {
            return buildConfigResource((ConfigRemoveRequest) request);
        }
        return RequestResource.configBuilder().build();
    }

    private RequestResource buildConfigResource(ConfigQueryRequest request) {
        return buildResource(request.getTenant(), request.getGroup(), request.getDataId());
    }

    private RequestResource buildConfigResource(ConfigPublishRequest request) {
        return buildResource(request.getTenant(), request.getGroup(), request.getDataId());
    }

    private RequestResource buildConfigResource(ConfigRemoveRequest request) {
        return buildResource(request.getTenant(), request.getGroup(), request.getDataId());
    }
}
