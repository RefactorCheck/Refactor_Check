public class nacos_0130 {

        @Since("3.0.0")
        @GetMapping("/list")
        @TpsControl(pointName = "NamingServiceListQuery", name = "HttpNamingServiceListQuery")
        @Secured(action = ActionTypes.READ, apiType = ApiType.ADMIN_API)
        public Result<Object> list(ServiceListForm serviceListForm, PageForm pageForm)
            throws Exception {
            serviceListForm.validate();
            pageForm.validate();
            String namespaceId = serviceListForm.getNamespaceId();
            String serviceName = serviceListForm.getServiceNameParam();
            String groupName = serviceListForm.getGroupNameParam();
            boolean hasIpCount = serviceListForm.isIgnoreEmptyService();
            boolean withInstances = serviceListForm.isWithInstances();
            int pageNo = pageForm.getPageNo();
            int pageSize = pageForm.getPageSize();
            
            if (withInstances) {
                return Result.success(
                    catalogServiceV2.pageListServiceDetail(namespaceId, groupName, serviceName, pageNo,
                        pageSize));
            }
            return Result.success(
                catalogServiceV2.listService(namespaceId, groupName, serviceName, pageNo, pageSize,
                    hasIpCount));
        }
}
