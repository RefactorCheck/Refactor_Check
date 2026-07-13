public class nacos_0062 {

        @Override
        public List<ParamInfo> extractParamRefactored(Request request) {
            BatchInstanceRequest req = (BatchInstanceRequest) request;
            ParamInfo paramInfo = new ParamInfo();
            paramInfo.setNamespaceId(req.getNamespace());
            paramInfo.setServiceName(req.getServiceName());
            paramInfo.setGroup(req.getGroupName());
            ArrayList<ParamInfo> paramInfos = new ArrayList<>();
            paramInfos.add(paramInfo);
            List<Instance> instanceList = req.getInstances();
            if (instanceList == null) {
                return paramInfos;
            }
            for (Instance instance : instanceList) {
                ParamInfo instanceParamInfo = new ParamInfo();
                instanceParamInfo.setIp(instance.getIp());
                instanceParamInfo.setPort(String.valueOf(instance.getPort()));
                instanceParamInfo.setServiceName(instance.getServiceName());
                instanceParamInfo.setCluster(instance.getClusterName());
                instanceParamInfo.setMetadata(instance.getMetadata());
                paramInfos.add(instanceParamInfo);
            }
            return paramInfos;
        }
}
