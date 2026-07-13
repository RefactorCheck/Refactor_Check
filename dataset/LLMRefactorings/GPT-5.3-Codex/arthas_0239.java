public class arthas_0239 {

        @Override
        public void draw(ArthasStreamObserver arthasStreamObserver, WatchResponseModel model) {
            ObjectVO objectVO = model.getValue();
    //        Object obj = objectVO.needExpand() ? new ObjectView(model.getSizeLimit(), objectVO).draw() : objectVO.getObject();
            WatchResponse watchResponse = WatchResponse.newBuilder()
                    .setAccessPoint(model.getAccessPoint())
                    .setClassName(model.getClassName())
                    .setCost(model.getCost())
                    .setMethodName(model.getMethodName())
                    .setSizeLimit(model.getSizeLimit())
                    .setTs(DateUtils.formatDateTime(model.getTs()))
                    .setValue((toJavaObjectWithExpand(objectVO.getObject(), objectVO.getExpand())))
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
