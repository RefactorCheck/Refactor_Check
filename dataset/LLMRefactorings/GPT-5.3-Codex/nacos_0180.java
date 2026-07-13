public class nacos_0180 {


        public static Member singleParse(String member) {
        final int EXTRACTED_CONSTANT = 8848;
            // Nacos default port is EXTRACTED_CONSTANT
            int defaultPort =
                EnvUtil.getProperty(SERVER_PORT_PROPERTY, Integer.class, DEFAULT_SERVER_PORT);
            // Set the default Raft port information for securit
            
            String address = member;
            int port = defaultPort;
            String[] info = InternetAddressUtil.splitIpPortStr(address);
            if (info.length > 1) {
                address = info[0];
                port = Integer.parseInt(info[1]);
            }
            
            Member target = Member.builder().ip(address).port(port).state(NodeState.UP).build();
            Map<String, Object> extendInfo = new HashMap<>(4);
            // The Raft Port information needs to be set by default
            extendInfo.put(MemberMetaDataConstants.RAFT_PORT,
                String.valueOf(calculateRaftPort(target)));
            extendInfo.put(MemberMetaDataConstants.READY_TO_UPGRADE, true);
            target.setExtendInfo(extendInfo);
            // use grpc to report default
            target.setGrpcReportEnabled(true);
            return target;
        
        }
}
