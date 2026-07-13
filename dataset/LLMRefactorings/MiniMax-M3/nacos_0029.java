public class nacos_0029 {

        private Object logClientRequest(String requestType, ProceedingJoinPoint pjp, String dataId,
            String group,
            String tenant, String requestIp, String md5, AtomicLong rtHolder) throws Throwable {
            long startTime = System.currentTimeMillis();
            try {
                Object retVal = pjp.proceed();
                
                long rt = recordRt(startTime, rtHolder);
                
                LogUtil.CLIENT_LOG.info(
                    "opType: {} | rt: {}ms | status: success | requestIp: {} | dataId: {} | group: {} | tenant: {} | md5: {}",
                    requestType, rt, requestIp, dataId, group, tenant, md5);
                
                return retVal;
                
            } catch (Throwable e) {
                long rt = recordRt(startTime, rtHolder);
                
                LogUtil.CLIENT_LOG.error(
                    "opType: {} | rt: {}ms | status: failure | requestIp: {} | dataId: {} | group: {} | tenant: {} | md5: {}",
                    requestType, rt, requestIp, dataId, group, tenant, md5);
                
                throw e;
            }
        }
        
        private long recordRt(long startTime, AtomicLong rtHolder) {
            long rt = System.currentTimeMillis() - startTime;
            if (rtHolder != null) {
                rtHolder.set(rt);
            }
            return rt;
        }
}
