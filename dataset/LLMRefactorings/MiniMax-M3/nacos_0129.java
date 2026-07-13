public class nacos_0129 {

            @Override
            public void run() {
                if (key != null && key.isValid()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    Beat beat = (Beat) key.attachment();
                    
                    if (channel.isConnected()) {
                        return;
                    }
                    
                    try {
                        channel.finishConnect();
                    } catch (Exception ignore) {
                    }
                    
                    handleTimeout(key, beat);
                }
            }
            
            private void handleTimeout(SelectionKey key, Beat beat) {
                try {
                    beat.finishCheck(false, false, beat.getTask().getCheckRtNormalized() * 2,
                        "tcp:timeout");
                    key.cancel();
                    key.channel().close();
                } catch (Exception ignore) {
                }
            }
}
