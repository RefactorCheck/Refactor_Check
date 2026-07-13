public class arthas_0235 {

    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final String CHARSET_NAME = "UTF-8";
    private static final String JAVA_PID_MARKER = "JAVA_PID";
    private static final String EXECUTION_TIMEOUT_MS = "2000";
    private static final int BUFFER_SIZE = 1024;

    private static long findProcessByTelnetClient(String arthasHomeDir, int telnetPort) {
        // start java telnet client
        List<String> telnetArgs = new ArrayList<String>();
        telnetArgs.add("-c");
        telnetArgs.add("session");
        telnetArgs.add("--execution-timeout");
        telnetArgs.add(EXECUTION_TIMEOUT_MS);
        // telnet port ,ip
        telnetArgs.add(DEFAULT_HOST);
        telnetArgs.add("" + telnetPort);

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
            String error = null;
            int status = ProcessUtils.startArthasClient(arthasHomeDir, telnetArgs, out);
            if (status == STATUS_EXEC_TIMEOUT) {
                error = "detection timeout";
            } else if (status == STATUS_EXEC_ERROR) {
                error = "detection error";
                AnsiLog.error("process status: {}", status);
                AnsiLog.error("process output: {}", out.toString());
            } else {
                // ignore connect error
            }
            if (error != null) {
                AnsiLog.error("The telnet port {} is used, but process {}, you will connect to an unexpected process.", telnetPort, error);
                AnsiLog.error("Try to use a different telnet port, for example: java -jar arthas-boot.jar --telnet-port 9998 --http-port -1");
                System.exit(1);
            }

            //parse output, find java pid
            String output = out.toString(CHARSET_NAME);
            String javaPidLine = null;
            Scanner scanner = new Scanner(output);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains(JAVA_PID_MARKER)) {
                    javaPidLine = line;
                    break;
                }
            }
            if (javaPidLine != null) {
                // JAVA_PID    10473
                try {
                    String[] strs = javaPidLine.split(JAVA_PID_MARKER);
                    if (strs.length > 1) {
                        return Long.parseLong(strs[strs.length - 1].trim());
                    }
                } catch (NumberFormatException e) {
                    // ignore
                }
            }
        } catch (Throwable ex) {
            AnsiLog.error("Detection telnet port error");
            AnsiLog.error(ex);
        }

        return -1;
    }
}
