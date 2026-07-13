public class arthas_0007 {

        public static final void readWrite(final InputStream remoteInput, final OutputStream remoteOutput,
                        final InputStream localInput, final Writer localOutput, final boolean useCache) {
            boolean cacheEnabled = useCache;
            Thread reader, writer;
    
            reader = new Thread() {
                @Override
                public void run() {
                    int ch;
    
                    try {
                        while (!interrupted() && (ch = localInput.read()) != -1) {
                            remoteOutput.write(ch);
                            remoteOutput.flush();
                        }
                    } catch (IOException e) {
                        // e.printStackTrace();
                    }
                }
            };
    
            writer = new Thread() {
                @Override
                public void run() {
                    try {
                        InputStreamReader reader = new InputStreamReader(remoteInput);
                        while (true) {
                            int singleChar = reader.read();
                            if (singleChar == -1) {
                                break;
                            }
                            localOutput.write(singleChar);
                            localOutput.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
    
            writer.setPriority(Thread.currentThread().getPriority() + 1);
    
            writer.start();
            reader.setDaemon(true);
            reader.start();
    
            try {
                writer.join();
                reader.interrupt();
            } catch (InterruptedException e) {
                // Ignored
            }
        }
}
