public class arthas_0281 {

    @Override
    public void run() {
        try {
            StringBuilder line = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            int b = -1;
            while (true) {
                b = in.read();
                if (b == -1) {
                    break;
                }
                line.appendCodePoint(b);
                handlePrompt(line);
                System.out.print(Character.toChars(b));
            }
        } catch (Exception e) {
        }
    }

    private void handlePrompt(StringBuilder line) throws InterruptedException {
        int index = line.indexOf(PROMPT);
        if (index >= 0) {
            line.delete(0, index + PROMPT.length());
            receviedPromptQueue.put("");
        }
    }
}
