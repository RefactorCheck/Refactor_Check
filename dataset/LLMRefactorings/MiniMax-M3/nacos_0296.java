public class nacos_0296 {

        @Override
        public void run() {
            while (true) {
                try {
                    processTask();
                    processSelectedKeys();
                } catch (Throwable e) {
                    SRV_LOG.error("[HEALTH-CHECK-V2] error while processing NIO task", e);
                }
            }
        }

        private void processSelectedKeys() {
            int readyCount = selector.selectNow();
            if (readyCount <= 0) {
                return;
            }

            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();

                GlobalExecutor.executeTcpSuperSense(new PostProcessor(key));
            }
        }
}
