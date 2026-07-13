public class arthas_0120 {

        public void parseRequestParams(WatchRequest watchRequest){
            this.classPattern = watchRequest.getClassPattern();
            this.methodPattern = watchRequest.getMethodPattern();
            if(StringUtils.isEmpty(watchRequest.getExpress())){
                this.express = "{params, target, returnObj}";
            }else {
                this.express = watchRequest.getExpress();
            }
            this.conditionExpress = watchRequest.getConditionExpress();
            this.isBefore = watchRequest.getIsBefore();
            this.isFinish = watchRequest.getIsFinish();
            this.isException = watchRequest.getIsException();
            this.isSuccess = watchRequest.getIsSuccess();
            if (!watchRequest.getIsBefore() && !watchRequest.getIsFinish() && !watchRequest.getIsException() && !watchRequest.getIsSuccess()) {
                this.isFinish = true;
            }
            if (watchRequest.getExpand() <= 0) {
                this.expand = 1;
            } else if (watchRequest.getExpand() > MAX_EXPAND){
                this.expand = MAX_EXPAND;
            } else {
                this.expand = watchRequest.getExpand();
            }
            if (watchRequest.getSizeLimit() == 0) {
                this.sizeLimit = 10 * 1024 * 1024;
            } else {
                this.sizeLimit = watchRequest.getSizeLimit();
            }
            this.isRegEx = watchRequest.getIsRegEx();
            if (watchRequest.getNumberOfLimit() == 0) {
                this.numberOfLimit = 100;
            } else {
                this.numberOfLimit = watchRequest.getNumberOfLimit();
            }
            if(watchRequest.getExcludeClassPattern().equals("")){
                this.excludeClassPattern = null;
            }else {
                this.excludeClassPattern = watchRequest.getExcludeClassPattern();
            }
            this.listenerId = watchRequest.getListenerId();
            this.verbose = watchRequest.getVerbose();
            if(watchRequest.getMaxNumOfMatchedClass() == 0){
                this.maxNumOfMatchedClass = 50;
            }else {
                this.maxNumOfMatchedClass = watchRequest.getMaxNumOfMatchedClass();
            }
            this.jobId = watchRequest.getJobId();
        }
}
