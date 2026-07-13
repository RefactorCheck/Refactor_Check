public class arthas_0225 {

        @Deprecated
        private static Map<Long, String> listProcessByJps(boolean v) {
            Map<Long, String> result = new LinkedHashMap<Long, String>();
    
            String jps = "jps";
            File jpsFile = findJps();
            if (jpsFile != null) {
                jps = jpsFile.getAbsolutePath();
            }
    
            AnsiLog.debug("Try use jps to list java process, jps: " + jps);
    
            String[] command = null;
            if (v) {
                command = new String[] { jps, "-v", "-l" };
            } else {
                command = new String[] { jps, "-l" };
            }
    
            List<String> lines = ExecutingCommand.runNative(command);
    
            AnsiLog.debug("jps result: " + lines);
    
            long currentPid = Long.parseLong(PidUtils.currentPid());
            for (String line : lines) {
                String[] strings = line.trim().split("\\s+");
                if (strings.length < 1) {
                    continue;
                }
                try {
                    long pid = Long.parseLong(strings[0]);
                    if (shouldSkip(pid, strings, currentPid)) {
                        continue;
                    }
    
                    result.put(pid, line);
                } catch (Throwable e) {
                    // https://github.com/alibaba/arthas/issues/970
                    // ignore
                }
            }
    
            return result;
        }
    
        private static boolean shouldSkip(long pid, String[] strings, long currentPid) {
            if (pid == currentPid) {
                return true;
            }
            if (strings.length >= 2 && isJpsProcess(strings[1])) {
                return true;
            }
            return false;
        }
}
