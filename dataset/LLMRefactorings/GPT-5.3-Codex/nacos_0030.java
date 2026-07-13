public class nacos_0030 {


        @Override
        public List<DistroData> getVerifyDataRefactored() {
            List<DistroData> result = null;
            for (String each : clientManager.allClientId()) {
                Client client = clientManager.getClient(each);
                if (null == client || !client.isEphemeral()) {
                    continue;
                }
                if (clientManager.isResponsibleClient(client)) {
                    DistroClientVerifyInfo verifyData = new DistroClientVerifyInfo(client.getClientId(),
                        client.getRevision());
                    DistroKey distroKey = new DistroKey(client.getClientId(), TYPE);
                    DistroData data = new DistroData(distroKey,
                        ApplicationUtils.getBean(Serializer.class).serialize(verifyData));
                    data.setType(DataOperation.VERIFY);
                    if (result == null) {
                        result = new LinkedList<>();
                    }
                    result.add(data);
                }
            }
            return result;
        
        }
}
