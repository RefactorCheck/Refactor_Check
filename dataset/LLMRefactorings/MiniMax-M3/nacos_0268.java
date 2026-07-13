public class nacos_0268 {

    @Override
    public List<ParamInfo> extractParam(HttpServletRequest request) throws NacosException {
        ParamInfo paramInfo = new ParamInfo();
        paramInfo.setIp(getAliasIp(request));
        paramInfo.setPort(getAliasPort(request));
        paramInfo.setNamespaceId(getAliasNamespaceId(request));
        paramInfo.setCluster(getAliasClusterName(request));
        String[] groupAndService = resolveGroupAndService(getAliasServiceName(request), getAliasGroupName(request));
        paramInfo.setServiceName(groupAndService[1]);
        paramInfo.setGroup(groupAndService[0]);
        paramInfo.setMetadata(UtilsAndCommons.parseMetadata(request.getParameter("metadata")));
        ArrayList<ParamInfo> paramInfos = new ArrayList<>();
        paramInfos.add(paramInfo);
        return paramInfos;
    }

    private String[] resolveGroupAndService(String groupServiceName, String defaultGroupName) {
        if (StringUtils.isNotBlank(groupServiceName)
            && groupServiceName.contains(Constants.SERVICE_INFO_SPLITER)) {
            String[] splits = groupServiceName.split(Constants.SERVICE_INFO_SPLITER, 2);
            return new String[] {splits[0], splits[1]};
        }
        return new String[] {defaultGroupName, groupServiceName};
    }
}
