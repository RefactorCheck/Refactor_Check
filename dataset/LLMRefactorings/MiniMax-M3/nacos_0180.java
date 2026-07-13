public class nacos_0180 {

        public static Member singleParse(String member) {
            int defaultPort =
                EnvUtil.getProperty(SERVER_PORT_PROPERTY, Integer.class, DEFAULT_SERVER_PORT);
            String address = member;
            int port = defaultPort;
            String[] info = InternetAddressUtil.splitIpPortStr(address);
            if (info.length > 1) {
                address = info[0];
                port = Integer.parseInt(info[1]);
            }
            
            Member target = Member.builder().ip(address).port(port).state(NodeState.UP).build();
            setDefaultExtendInfo(target);
            target.setGrpcReportEnabled(true);
            return target;
        }

        private static void setDefaultExtendInfo(Member target) {
            Map<String, Object> extendInfo = new HashMap<>(4);
            extendInfo.put(MemberMetaDataConstants.RAFT_PORT,
                String.valueOf(calculateRaftPort(target)));
            extendInfo.put(MemberMetaDataConstants.READY_TO_UPGRADE, true);
            target.setExtendInfo(extendInfo);
        }
}
