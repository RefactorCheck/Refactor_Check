public class nacos_0171 {

        private void verifyForDataStorage(String type, List<Member> targetServer) {
            DistroDataStorage dataStorage = distroComponentHolder.findDataStorage(type);
            if (!dataStorage.isFinishInitial()) {
                Loggers.DISTRO.warn(
                    "data storage {} has not finished initial step, do not send verify data",
                    dataStorage.getClass().getSimpleName());
                return;
            }
            List<DistroData> verifyData = dataStorage.getVerifyData();
            if (null == verifyData || verifyData.isEmpty()) {
                return;
            }
            for (Member member : targetServer) {
                scheduleVerifyTask(type, member, verifyData);
            }
        }

        private void scheduleVerifyTask(String type, Member member, List<DistroData> verifyData) {
            DistroTransportAgent agent = distroComponentHolder.findTransportAgent(type);
            if (null == agent) {
                return;
            }
            String address = member.getAddress();
            executeTaskExecuteEngine.addTask(address + type,
                new DistroVerifyExecuteTask(agent, verifyData, address, type));
        }
}
