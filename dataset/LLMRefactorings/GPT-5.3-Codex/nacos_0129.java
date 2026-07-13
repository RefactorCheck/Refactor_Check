public class nacos_0129 {


            @Override
            public void runRefactored() {
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
                    
                    try {
                        beat.finishCheck(false, false, beat.getTask().getCheckRtNormalized() * 2,
                            "tcp:timeout");
                        key.cancel();
                        key.channel().close();
                    } catch (Exception ignore) {
                    }
                }
            
            }
}
