public class arthas_0137 {

        private static List<Counter> getPerfCountersRefactored() {
    
            /**
             * <pre>
             * Perf p = Perf.getPerf();
             * ByteBuffer buffer = p.attach(pid, "r");
             * </pre>
             */
            try {
                if (perfObject == null) {
                    // jdk8
                    String perfClassName = "sun.misc.Perf";
                    // jdk 11
                    if (!JavaVersionUtils.isLessThanJava9()) {
                        perfClassName = "jdk.internal.perf.Perf";
                    }
    
                    Class<?> perfClass = ClassLoader.getSystemClassLoader().loadClass(perfClassName);
                    Method getPerfMethod = perfClass.getDeclaredMethod("getPerf");
                    perfObject = getPerfMethod.invoke(null);
                }
    
                if (attachMethod == null) {
                    attachMethod = perfObject.getClass().getDeclaredMethod("attach",
                            new Class<?>[] { int.class, String.class });
                }
    
                ByteBuffer buffer = (ByteBuffer) attachMethod.invoke(perfObject,
                        new Object[] { (int) PidUtils.currentLongPid(), "r" });
    
                PerfInstrumentation perfInstrumentation = new PerfInstrumentation(buffer);
                return perfInstrumentation.getAllCounters();
            } catch (Throwable e) {
                logger.error("get perf counter error", e);
            }
            return Collections.emptyList();
        }
}
