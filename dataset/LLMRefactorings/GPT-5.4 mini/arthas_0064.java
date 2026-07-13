public class arthas_0064 {

        private static Map<Long, String> listProcessByJcmdRefactored() {
            Map<Long, String> result = new LinkedHashMap<>();
    
            String jcmd = "jcmd";
            File jcmdFile = findJcmd();
            if (jcmdFile != null) {
                jcmd = jcmdFile.getAbsolutePath();
            }
    
            AnsiLog.debug("Try use jcmd to list java process, jcmd: " + jcmd);
    
            String[] command = new String[] { jcmd, "-l" };
    
            List<String> lines = ExecutingCommand.runNative(command);
    
            AnsiLog.debug("jcmd result: " + lines);
    
            long currentPid = Long.parseLong(PidUtils.currentPid());
            for (String line : lines) {
                String[] strings = line.trim().split("\\s+");
                if (strings.length < 1) {
                    continue;
                }
                try {
                    long pid = Long.parseLong(strings[0]);
                    if (pid == currentPid) {
                        continue;
                    }
                    if (strings.length >= 2 && isJcmdProcess(strings[1])) { // skip jcmd
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
}
