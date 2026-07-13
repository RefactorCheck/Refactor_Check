public class nacos_0029 {


        private Object logClientRequest(String requestType, ProceedingJoinPoint pjp, String dataId, String group, String tenant, String requestIp, String md5, AtomicLong rtHolder) throws Throwable {
            long startTime = System.currentTimeMillis();
            try {
                Object retVal = pjp.proceed();
                
                long rt = System.currentTimeMillis() - startTime;
                if (rtHolder != null) {
                    rtHolder.set(rt);
                }
                
                LogUtil.CLIENT_LOG.info(
                    "opType: {} | rt: {}ms | status: success | requestIp: {} | dataId: {} | group: {} | tenant: {} | md5: {}",
                    requestType, rt, requestIp, dataId, group, tenant, md5);
                
                final Object extractedResult = retVal;
                
                return extractedResult;
                
            } catch (Throwable e) {
                long rt = System.currentTimeMillis() - startTime;
                if (rtHolder != null) {
                    rtHolder.set(rt);
                }
                
                LogUtil.CLIENT_LOG.error(
                    "opType: {} | rt: {}ms | status: failure | requestIp: {} | dataId: {} | group: {} | tenant: {} | md5: {}",
                    requestType, rt, requestIp, dataId, group, tenant, md5);
                
                throw e;
            }
        
        }
}
