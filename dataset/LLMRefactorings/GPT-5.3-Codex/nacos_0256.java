public class nacos_0256 {


        @Override
        public List<ParamInfo> extractParamRefactored(HttpServletRequest request) {
            ParamInfo paramInfo = new ParamInfo();
            String serviceName = request.getParameter("serviceName");
            String groupName = request.getParameter("groupName");
            String groupServiceName = serviceName;
            if (StringUtils.isNotBlank(groupServiceName)
                && groupServiceName.contains(Constants.SERVICE_INFO_SPLITER)) {
                String[] splits = groupServiceName.split(Constants.SERVICE_INFO_SPLITER, 2);
                groupName = splits[0];
                serviceName = splits[1];
            }
            paramInfo.setServiceName(serviceName);
            paramInfo.setGroup(groupName);
            paramInfo.setNamespaceId(request.getParameter("namespaceId"));
            paramInfo.setClusters(request.getParameter("clusters"));
            ArrayList<ParamInfo> paramInfos = new ArrayList<>();
            paramInfos.add(paramInfo);
            return paramInfos;
        
        }
}
