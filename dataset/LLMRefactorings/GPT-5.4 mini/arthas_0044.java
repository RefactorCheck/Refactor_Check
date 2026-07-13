public class arthas_0044 {

        private static void watching(Advice advice) {
            try {
                // 本次调用的耗时
                System.out.println("************job:  "+ arthasStreamObserver.getJobId() + "  rpc watch advice开始正式执行,执行信息如下*****************");
                System.out.println("listener ID: + " + arthasStreamObserver.getListener().id());
                System.out.println("参数: \n" + watchRequestModel.toString());
                System.out.println("###################***************** \n\n");
                double cost = threadLocalWatch.costInMillis();
                boolean conditionResult = isConditionMet(watchRequestModel.getConditionExpress(), advice, cost);
                if (this.isVerbose()) {
                    String msg = "Condition express: " + watchRequestModel.getConditionExpress() + " , result: " + conditionResult + "\n";
                    arthasStreamObserver.appendResult(new MessageModel(msg));
                }
                if (conditionResult) {
                    long resultId = idGenerator.incrementAndGet();
                    results.put(resultId, advice);
                    Object value = getExpressionResult(watchRequestModel.getExpress(), advice, cost);
    
                    WatchResponseModel model = new WatchResponseModel();
                    model.setResultId(resultId);
                    model.setTs(LocalDateTime.now());
                    model.setCost(cost);
                    model.setValue(new ObjectVO(value, watchRequestModel.getExpand()));
                    model.setSizeLimit(watchRequestModel.getSizeLimit());
                    model.setClassName(advice.getClazz().getName());
                    model.setMethodName(advice.getMethod().getName());
                    if (advice.isBefore()) {
                        model.setAccessPoint(AccessPoint.ACCESS_BEFORE.getKey());
                    } else if (advice.isAfterReturning()) {
                        model.setAccessPoint(AccessPoint.ACCESS_AFTER_RETUNING.getKey());
                    } else if (advice.isAfterThrowing()) {
                        model.setAccessPoint(AccessPoint.ACCESS_AFTER_THROWING.getKey());
                    }
                    arthasStreamObserver.appendResult(model);
                    arthasStreamObserver.times().incrementAndGet();
                    if (isLimitExceeded(watchRequestModel.getNumberOfLimit(), arthasStreamObserver.times().get())) {
                        String msg = "Command execution times exceed limit: " + watchRequestModel.getNumberOfLimit()
                                + ", so command will exit.\n";
                        arthasStreamObserver.end();
                    }
                }
            } catch (Throwable e) {
                logger.warn("watch failed.", e);
                arthasStreamObserver.end(-1, "watch failed, condition is: " + watchRequestModel.getConditionExpress() + ", express is: "
                        + watchRequestModel.getExpress() + ", " + e.getMessage() + ", visit " + LogUtil.loggingFile()
                        + " for more details.");
            }
        }
}
