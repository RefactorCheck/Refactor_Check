public class arthas_0239 {

        @Override
        public void draw(ArthasStreamObserver arthasStreamObserver, WatchResponseModel model) {
            ObjectVO objectVO = model.getValue();
            JavaObject javaObject = toJavaObjectWithExpand(objectVO.getObject(), objectVO.getExpand());
            WatchResponse watchResponse = buildWatchResponse(model, javaObject);
            ResponseBody responseBody = buildResponseBody(model, watchResponse);
            arthasStreamObserver.onNext(responseBody);
        }

        private WatchResponse buildWatchResponse(WatchResponseModel model, JavaObject javaObject) {
            return WatchResponse.newBuilder()
                    .setAccessPoint(model.getAccessPoint())
                    .setClassName(model.getClassName())
                    .setCost(model.getCost())
                    .setMethodName(model.getMethodName())
                    .setSizeLimit(model.getSizeLimit())
                    .setTs(DateUtils.formatDateTime(model.getTs()))
                    .setValue(javaObject)
                    .build();
        }

        private ResponseBody buildResponseBody(WatchResponseModel model, WatchResponse watchResponse) {
            return ResponseBody.newBuilder()
                    .setJobId(model.getJobId())
                    .setResultId(model.getResultId())
                    .setType(model.getType())
                    .setWatchResponse(watchResponse)
                    .build();
        }
}
