public class arthas_0002 {

        @Override
        public void update(StringStringMapValue request, StreamObserver<ResponseBody> responseObserver){
            Map<String, String> properties = request.getStringStringMapMap();
            String propertyName = "";
            String propertyValue = "";
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                propertyName = entry.getKey();
                propertyValue = entry.getValue();
            }
            ArthasStreamObserver<ResponseBody> arthasStreamObserver = new ArthasStreamObserverImpl<>(responseObserver,null, grpcJobController);
            arthasStreamObserver.setProcessStatus(ExecStatus.RUNNING);
            try {
                setSystemProperty(propertyName, propertyValue, arthasStreamObserver);
            }catch (Throwable t) {
                arthasStreamObserver.end(-1, "Error during setting system property: " + t.getMessage());
            }
        }

        private void setSystemProperty(String propertyName, String propertyValue, ArthasStreamObserver<ResponseBody> arthasStreamObserver) {
            System.setProperty(propertyName, propertyValue);
            arthasStreamObserver.appendResult(new SystemPropertyModel(propertyName, System.getProperty(propertyName)));
            arthasStreamObserver.onCompleted();
        }
}
