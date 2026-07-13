public class netty_0161 {

        static int processHandlePid(ClassLoader loader) {
            // pid is positive on unix, non{-1,0} on windows
            int nilValue = -1;
            if (PlatformDependent.javaVersion() >= 9) {
                Long pid;
                try {
                    pid = invokeProcessHandlePid(loader);
                } catch (Exception e) {
                    logger.debug("Could not invoke ProcessHandle.current().pid();", e);
                    return nilValue;
                }
                if (pid > Integer.MAX_VALUE || pid < Integer.MIN_VALUE) {
                    throw new IllegalStateException("Current process ID exceeds int range: " + pid);
                }
                return pid.intValue();
            }
            return nilValue;
        }

        private static Long invokeProcessHandlePid(ClassLoader loader) throws Exception {
            Class<?> processHandleImplType = Class.forName("java.lang.ProcessHandle", true, loader);
            Method processHandleCurrent = processHandleImplType.getMethod("current");
            Object processHandleInstance = processHandleCurrent.invoke(null);
            Method processHandlePid = processHandleImplType.getMethod("pid");
            return (Long) processHandlePid.invoke(processHandleInstance);
        }
}
