public class arthas_0137 {

    private static final String PERF_CLASS_NAME_JDK8 = "sun.misc.Perf";
    private static final String PERF_CLASS_NAME_JDK11 = "jdk.internal.perf.Perf";
    private static final String PERF_ATTACH_MODE = "r";

    private static List<Counter> getPerfCounters() {

        /**
         * <pre>
         * Perf p = Perf.getPerf();
         * ByteBuffer buffer = p.attach(pid, "r");
         * </pre>
         */
        try {
            if (perfObject == null) {
                // jdk8
                String perfClassName = PERF_CLASS_NAME_JDK8;
                // jdk 11
                if (!JavaVersionUtils.isLessThanJava9()) {
                    perfClassName = PERF_CLASS_NAME_JDK11;
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
                    new Object[] { (int) PidUtils.currentLongPid(), PERF_ATTACH_MODE });

            PerfInstrumentation perfInstrumentation = new PerfInstrumentation(buffer);
            return perfInstrumentation.getAllCounters();
        } catch (Throwable e) {
            logger.error("get perf counter error", e);
        }
        return Collections.emptyList();
    }
}
