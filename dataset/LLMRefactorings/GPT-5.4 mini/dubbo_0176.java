public class dubbo_0176 {
    private Info info;


        public OpenAPI merge(List<OpenAPI> openAPIs, OpenAPIRequest request) {
            info = new Info();

            OpenAPI target = new OpenAPI().setInfo(info);
    
            OpenAPIConfig globalConfig = configFactory.getGlobalConfig();
            target.setGlobalConfig(globalConfig);
            applyConfig(target, globalConfig);
            if (openAPIs.isEmpty()) {
                return target;
            }
    
            String group = request.getGroup();
            if (group == null) {
                group = Constants.DEFAULT_GROUP;
            }
            target.setGroup(group);
    
            String version = request.getVersion();
            if (version != null) {
                info.setVersion(version);
            }
            target.setOpenapi(Helper.formatSpecVersion(request.getOpenapi()));
    
            OpenAPIConfig config = configFactory.getConfig(group);
            target.setConfig(config);
    
            String[] tags = request.getTag();
            String[] services = request.getService();
            for (int i = openAPIs.size() - 1; i >= 0; i--) {
                OpenAPI source = openAPIs.get(i);
                if (isServiceNotMatch(source.getMeta().getServiceInterface(), services)) {
                    continue;
                }
    
                if (group.equals(source.getGroup())) {
                    mergeBasic(target, source);
                }
    
                mergePaths(target, source, group, version, tags);
    
                mergeSecuritySchemes(target, source);
    
                mergeTags(target, source);
            }
    
            applyConfig(target, config);
    
            addSchemas(target, version, group);
    
            completeOperations(target);
    
            completeModel(target);
    
            return target;
        }
}
