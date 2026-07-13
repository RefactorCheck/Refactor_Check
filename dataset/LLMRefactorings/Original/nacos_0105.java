public class nacos_0105 {

            @Override
            public Void call() {
                long waited = System.currentTimeMillis() - beat.getStartTime();
                if (waited > MAX_WAIT_TIME_MILLISECONDS) {
                    Loggers.SRV_LOG.warn("beat task waited too long: " + waited + "ms");
                }
                
                SocketChannel channel = null;
                try {
                    HealthCheckInstancePublishInfo instance = beat.getInstance();
                    
                    BeatKey beatKey = keyMap.get(beat.toString());
                    if (beatKey != null && beatKey.key.isValid()) {
                        if (System.currentTimeMillis() - beatKey.birthTime < TCP_KEEP_ALIVE_MILLIS) {
                            instance.finishCheck();
                            return null;
                        }
                        
                        beatKey.key.cancel();
                        beatKey.key.channel().close();
                    }
                    
                    channel = SocketChannel.open();
                    channel.configureBlocking(false);
                    // only by setting this can we make the socket close event asynchronous
                    channel.socket().setSoLinger(false, -1);
                    channel.socket().setReuseAddress(true);
                    channel.socket().setKeepAlive(true);
                    channel.socket().setTcpNoDelay(true);
                    
                    ClusterMetadata cluster = beat.getMetadata();
                    int port = cluster.isUseInstancePortForCheck() ? instance.getPort()
                        : cluster.getHealthyCheckPort();
                    channel.connect(new InetSocketAddress(instance.getIp(), port));
                    
                    SelectionKey key =
                        channel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ);
                    key.attach(beat);
                    keyMap.put(beat.toString(), new BeatKey(key));
                    
                    beat.setStartTime(System.currentTimeMillis());
                    
                    GlobalExecutor
                        .scheduleTcpSuperSenseTask(new TimeOutTask(key), CONNECT_TIMEOUT_MS,
                            TimeUnit.MILLISECONDS);
                } catch (Exception e) {
                    beat.finishCheck(false, false, switchDomain.getTcpHealthParams().getMax(),
                        "tcp:error:" + e.getMessage());
                    
                    if (channel != null) {
                        try {
                            channel.close();
                        } catch (Exception ignore) {
                        }
                    }
                }
                
                return null;
            }
}
