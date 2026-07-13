public class nacos_0268 {

        @Override
        public List<ParamInfo> extractParam(HttpServletRequest request) throws NacosException {
            ParamInfo paramInfo = new ParamInfo();
            paramInfo.setIp(getAliasIp(request));
            paramInfo.setPort(getAliasPort(request));
            paramInfo.setNamespaceId(getAliasNamespaceId(request));
            paramInfo.setCluster(getAliasClusterName(request));
            String serviceName = getAliasServiceName(request);
            String groupName = getAliasGroupName(request);
            String groupServiceName = serviceName;
            if (StringUtils.isNotBlank(groupServiceName)
                && groupServiceName.contains(Constants.SERVICE_INFO_SPLITER)) {
                String[] splits = groupServiceName.split(Constants.SERVICE_INFO_SPLITER, 2);
                groupName = splits[0];
                serviceName = splits[1];
            }
            paramInfo.setServiceName(serviceName);
            paramInfo.setGroup(groupName);
            paramInfo.setMetadata(UtilsAndCommons.parseMetadata(request.getParameter("metadata")));
            ArrayList<ParamInfo> paramInfos = new ArrayList<>();
            paramInfos.add(paramInfo);
            return paramInfos;
        }
}
