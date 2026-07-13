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
                DistroTransportAgent agent = distroComponentHolder.findTransportAgent(type);
                if (null == agent) {
                    continue;
                }
                executeTaskExecuteEngine.addTask(member.getAddress() + type,
                    new DistroVerifyExecuteTask(agent, verifyData, member.getAddress(), type));
            }
        }
}
