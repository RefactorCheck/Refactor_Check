public class arthas_0057 {

        private void afterFinishing(Advice advice) {
            double cost = threadLocalWatch.costInMillis();
            TimeFragment timeTunnel = new TimeFragment(advice, LocalDateTime.now(), cost);
    
            if (!checkCondition(advice, cost)) {
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
        
        private boolean checkCondition(Advice advice, double cost) {
            String conditionExpress = command.getConditionExpress();
            boolean match = false;
            try {
                match = isConditionMet(conditionExpress, advice, cost);
                if (this.isVerbose()) {
                    process.write("Condition express: " + conditionExpress + " , result: " + match + "\n");
                }
            } catch (ExpressException e) {
                logger.warn("tt failed.", e);
                process.end(-1, "tt failed, condition is: " + conditionExpress + ", " + e.getMessage()
                              + ", visit " + LogUtil.loggingFile() + " for more details.");
            }
            return match;
        }
}
