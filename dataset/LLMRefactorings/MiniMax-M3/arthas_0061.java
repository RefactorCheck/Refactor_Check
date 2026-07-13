public class arthas_0061 {

        @Override
        public void atLine(ClassLoader loader, Class<?> clazz, ArthasMethod method, Object target, Object[] args,
                int lineNumber, String[] argNames, Object[] localVars, String[] localVarNames) throws Throwable {
            Advice advice = Advice.newForLine(loader, clazz, method, target, args, lineNumber, argNames, localVars,
                    localVarNames);
            try {
                double cost = threadLocalWatch.costInMillisWithoutPop();
                boolean conditionResult = isConditionMet(command.getConditionExpress(), advice, cost);
                if (this.isVerbose()) {
                    process.write("Condition express: " + command.getConditionExpress() + " , result: "
                            + conditionResult + "\n");
                }
                if (conditionResult) {
                    Object value = getExpressionResult(command.getExpress(), advice, cost);

                    process.appendResult(buildLineModel(loader, advice, cost, value, lineNumber));
                    process.times().incrementAndGet();
                    if (isLimitExceeded(command.getNumberOfLimit(), process.times().get())) {
                        abortProcess(process, command.getNumberOfLimit());
                    }
                }
            } catch (Throwable e) {
                logger.warn("line failed.", e);
                process.end(-1, "line failed, condition is: " + command.getConditionExpress() + ", express is: "
                        + command.getExpress() + ", " + e.getMessage() + ", visit " + LogUtil.loggingFile()
                        + " for more details.");
            }
        }

        private LineModel buildLineModel(ClassLoader loader, Advice advice, double cost, Object value,
                int lineNumber) {
            Thread currentThread = Thread.currentThread();
            LineModel model = new LineModel();
            model.setTs(LocalDateTime.now());
            model.setCost(cost);
            model.setValue(new ObjectVO(value, command.getExpand()));
            model.setSizeLimit(command.getSizeLimit());
            model.setClassName(advice.getClazz().getName());
            model.setMethodName(advice.getMethod().getName());
            model.setMethodDesc(advice.getMethod().getDescriptor());
            model.setLineNumber(lineNumber);
            model.setThreadName(currentThread.getName());
            model.setThreadId(currentThread.getId());
            if (command.isStack()) {
                model.setStackTrace(limitedStackTrace(loader, currentThread, command.getStackDepth()));
            }
            return model;
        }
}
