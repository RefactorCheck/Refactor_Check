public class arthas_0112 {

        private ExitStatus processExactMatch(CommandProcess process, RowAffect affect, Instrumentation inst,
                                       Set<Class<?>> matchedClasses) {
            Matcher<String> fieldNameMatcher = fieldNameMatcher();
    
            Class<?> clazz = matchedClasses.iterator().next();
    
            boolean found = false;
    
            for (Field field : clazz.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers()) || !fieldNameMatcher.matching(field.getName())) {
                    continue;
                }
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                try {
                    Object value = field.get(null);
    
                    if (!StringUtils.isEmpty(express)) {
                        value = ExpressFactory.threadLocalExpress(value).get(express);
                    }
    
                    process.appendResult(new GetStaticModel(field.getName(), value, expand));
    
                    affect.rCnt(1);
                } catch (IllegalAccessException e) {
                    logger.warn("getstatic: failed to get static value, class: {}, field: {} ", clazz, field.getName(), e);
                    process.appendResult(new MessageModel("Failed to get static, exception message: " + e.getMessage()
                                  + ", please check $HOME/logs/arthas/arthas.log for more details. "));
                } catch (ExpressException e) {
                    logger.warn("getstatic: failed to get express value, class: {}, field: {}, express: {}", clazz, field.getName(), express, e);
                    process.appendResult(new MessageModel("Failed to get static, exception message: " + e.getMessage()
                                  + ", please check $HOME/logs/arthas/arthas.log for more details. "));
                } finally {
                    found = true;
                }
            }
    
            if (!found) {
                return ExitStatus.failure(-1, "getstatic: no matched static field was found");
            } else {
                return ExitStatus.success();
            }
        }
}
