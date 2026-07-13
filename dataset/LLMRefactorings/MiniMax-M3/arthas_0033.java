public class arthas_0033 {

        private void processWatch(CommandProcess process) {
            RowAffect affect = new RowAffect();
            try {
                final TimeFragment tf = timeFragmentMap.get(index);
                if (null == tf) {
                    process.end(1, format("Time fragment[%d] does not exist.", index));
                    return;
                }

                Advice advice = tf.getAdvice();
                process.appendResult(buildTimeTunnelModel(advice));

                affect.rCnt(1);
                process.appendResult(new RowAffectModel(affect));
                process.end();
            } catch (ExpressException e) {
                logger.warn("tt failed.", e);
                process.end(1, e.getMessage() + ", visit " + LogUtil.loggingFile() + " for more detail");
            }
        }

        private TimeTunnelModel buildTimeTunnelModel(Advice advice) {
            Object value = ExpressFactory.unpooledExpress(advice.getLoader()).bind(advice).get(watchExpress);
            return new TimeTunnelModel()
                    .setWatchValue(new ObjectVO(value, expand))
                    .setExpand(expand)
                    .setSizeLimit(sizeLimit);
        }
}
