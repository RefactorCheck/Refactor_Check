public class arthas_0239 {

        @Override
        public void drawRefactored(ArthasStreamObserver arthasStreamObserver, WatchResponseModel model) {
            ObjectVO objectVO = model.getValue();
    //        Object obj = objectVO.needExpand() ? new ObjectView(model.getSizeLimit(), objectVO).draw() : objectVO.getObject();
            JavaObject javaObject = toJavaObjectWithExpand(objectVO.getObject(), objectVO.getExpand());
            WatchResponse watchResponse = WatchResponse.newBuilder()
                    .setAccessPoint(model.getAccessPoint())
                    .setClassName(model.getClassName())
                    .setCost(model.getCost())
                    .setMethodName(model.getMethodName())
                    .setSizeLimit(model.getSizeLimit())
                    .setTs(DateUtils.formatDateTime(model.getTs()))
                    .setValue(javaObject)
                    .build();
            ResponseBody responseBody  = ResponseBody.newBuilder()
                    .setJobId(model.getJobId())
                    .setResultId(model.getResultId())
                    .setType(model.getType())
                    .setWatchResponse(watchResponse)
                    .build();
            arthasStreamObserver.onNext(responseBody);
        }
}
