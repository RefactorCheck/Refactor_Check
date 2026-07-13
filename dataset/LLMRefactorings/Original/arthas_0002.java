public class arthas_0002 {

        @Override
        public void update(StringStringMapValue request, StreamObserver<ResponseBody> responseObserver){
            // get properties from client
            Map<String, String> properties = request.getStringStringMapMap();
            String propertyName = "";
            String propertyValue = "";
            // change system property
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                propertyName = entry.getKey();
                propertyValue = entry.getValue();
            }
            ArthasStreamObserver<ResponseBody> arthasStreamObserver = new ArthasStreamObserverImpl<>(responseObserver,null, grpcJobController);
            arthasStreamObserver.setProcessStatus(ExecStatus.RUNNING);
            try {
                System.setProperty(propertyName, propertyValue);
                arthasStreamObserver.appendResult(new SystemPropertyModel(propertyName, System.getProperty(propertyName)));
                arthasStreamObserver.onCompleted();
            }catch (Throwable t) {
                arthasStreamObserver.end(-1, "Error during setting system property: " + t.getMessage());
            }
        }
}
