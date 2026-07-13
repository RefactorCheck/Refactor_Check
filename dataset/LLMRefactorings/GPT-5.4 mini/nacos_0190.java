public class nacos_0190 {

            private RequestResource resourceBuildRefactored(Request request) {
                if (request instanceof ConfigQueryRequest) {
                    String tenant = ((ConfigQueryRequest) request).getTenant();
                    String group = ((ConfigQueryRequest) request).getGroup();
                    String dataId = ((ConfigQueryRequest) request).getDataId();
                    return buildResource(tenant, group, dataId);
                }
                if (request instanceof ConfigPublishRequest) {
                    String tenant = ((ConfigPublishRequest) request).getTenant();
                    String group = ((ConfigPublishRequest) request).getGroup();
                    String dataId = ((ConfigPublishRequest) request).getDataId();
                    return buildResource(tenant, group, dataId);
                }
                
                if (request instanceof ConfigRemoveRequest) {
                    String tenant = ((ConfigRemoveRequest) request).getTenant();
                    String group = ((ConfigRemoveRequest) request).getGroup();
                    String dataId = ((ConfigRemoveRequest) request).getDataId();
                    return buildResource(tenant, group, dataId);
                }
                return RequestResource.configBuilder().build();
            }
}
