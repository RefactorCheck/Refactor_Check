public class nacos_0256 {

        @Override
        public List<ParamInfo> extractParam(HttpServletRequest request) {
            ParamInfo paramInfo = new ParamInfo();
            String[] groupAndService = resolveGroupAndService(request);
            paramInfo.setServiceName(groupAndService[1]);
            paramInfo.setGroup(groupAndService[0]);
            paramInfo.setNamespaceId(request.getParameter("namespaceId"));
            paramInfo.setClusters(request.getParameter("clusters"));
            ArrayList<ParamInfo> paramInfos = new ArrayList<>();
            paramInfos.add(paramInfo);
            return paramInfos;
        }

        private String[] resolveGroupAndService(HttpServletRequest request) {
            String serviceName = request.getParameter("serviceName");
            String groupName = request.getParameter("groupName");
            if (StringUtils.isNotBlank(serviceName) && serviceName.contains(Constants.SERVICE_INFO_SPLITER)) {
                String[] splits = serviceName.split(Constants.SERVICE_INFO_SPLITER, 2);
                groupName = splits[0];
                serviceName = splits[1];
            }
            return new String[]{groupName, serviceName};
        }
}
