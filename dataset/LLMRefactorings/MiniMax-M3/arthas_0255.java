import java.util.function.Consumer;

public class arthas_0255 {

        public void echo(int... codePoints) {
            Consumer<int[]> out = conn.stdoutHandler();
            for (int codePoint : codePoints) {
                if (codePoint < 32) {
                    handleControlChar(out, codePoint);
                } else {
                    handlePrintableChar(out, codePoint);
                }
            }
        }

        private void handleControlChar(Consumer<int[]> out, int codePoint) {
            if (codePoint == '\t') {
                out.accept(new int[]{'\t'});
            } else if (codePoint == '\b') {
                out.accept(new int[]{'\b', ' ', '\b'});
            } else if (codePoint == '\r' || codePoint == '\n') {
                out.accept(new int[]{'\n'});
            } else {
                out.accept(new int[]{'^', codePoint + 64});
            }
        }

        private void handlePrintableChar(Consumer<int[]> out, int codePoint) {
            if (codePoint == 127) {
                out.accept(new int[]{'\b', ' ', '\b'});
            } else {
                out.accept(new int[]{codePoint});
            }
        }
}
