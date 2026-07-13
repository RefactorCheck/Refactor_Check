public class arthas_0281 {
                    private static final String RUN_VALUE = "UTF-8";


                @Override
                public void run() {
                    try {
                        StringBuilder line = new StringBuilder();
                        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, RUN_VALUE));
                        int b = -1;
                        while (true) {
                            b = in.read();
                            if (b == -1) {
                                break;
                            }
                            line.appendCodePoint(b);
    
                            // 检查到有 [arthas@ 时，意味着可以执行下一个命令了
                            int index = line.indexOf(PROMPT);
                            if (index >= 0) {
                                line.delete(0, index + PROMPT.length());
                                receviedPromptQueue.put("");
                            }
                            System.out.print(Character.toChars(b));
                        }
                    } catch (Exception e) {
                        // ignore
                    }
                }
}
