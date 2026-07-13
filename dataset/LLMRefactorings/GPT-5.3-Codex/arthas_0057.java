public class arthas_0057 {

        private void afterFinishing(Advice advice) {
            refactorExtractedMethod();
            TimeFragment timeTunnel = new TimeFragment(advice, LocalDateTime.now(), cost);
    
            boolean match = false;
            try {
                match = isConditionMet(command.getConditionExpress(), advice, cost);
                if (this.isVerbose()) {
                    process.write("Condition express: " + command.getConditionExpress() + " , result: " + match + "\n");
                }
            } catch (ExpressException e) {
                logger.warn("tt failed.", e);
                process.end(-1, "tt failed, condition is: " + command.getConditionExpress() + ", " + e.getMessage()
                              + ", visit " + LogUtil.loggingFile() + " for more details.");
            }
    
            if (!match) {
                return;
            }
    
            int index = command.putTimeTunnel(timeTunnel);
    
            TimeFragmentVO timeFragmentVO = TimeTunnelCommand.createTimeFragmentVO(index, timeTunnel, command.getExpand());
            TimeTunnelModel timeTunnelModel = new TimeTunnelModel()
                    .setTimeFragmentList(Collections.singletonList(timeFragmentVO))
                    .setFirst(isFirst);
            process.appendResult(timeTunnelModel);
    
            if (isFirst) {
                isFirst = false;
            }
    
            process.times().incrementAndGet();
            if (isLimitExceeded(command.getNumberOfLimit(), process.times().get())) {
                abortProcess(process, command.getNumberOfLimit());
            }
        }

        private void refactorExtractedMethod() {
            double cost = threadLocalWatch.costInMillis();
        }
}
