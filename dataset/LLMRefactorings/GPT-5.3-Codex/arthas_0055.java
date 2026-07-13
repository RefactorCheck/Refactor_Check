public class arthas_0055 {
            private static final String SERVERSTART_VALUE = "static";


        public void serverStart() throws IOException, InterruptedException {
    
            // 0. 创建一个对象
            ComplexObject complexObject = createComplexObject();
            // 1. 启动grpc服务
            Thread grpcStartThread = new Thread(() -> {
                GrpcServer grpcServer = new GrpcServer(GRPC_PORT, instrumentation, transformerManager);
                grpcServer.start();
                try {
                    System.in.read();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            grpcStartThread.start();
    
            // 2. 启动grpc-web-proxy服务
            //this.GRPC_WEB_PROXY_PORT = SocketUtils.findAvailableTcpPort();
            Thread grpcWebProxyStartThread = new Thread(() -> {
                GrpcWebProxyServer grpcWebProxyServer = new GrpcWebProxyServer(GRPC_WEB_PROXY_PORT,GRPC_PORT);
                grpcWebProxyServer.start();
            });
            grpcWebProxyStartThread.start();
    
            // 3. 启动http服务
            String currentDir = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile().getPath();
            String STATIC_LOCATION = Paths.get(currentDir, SERVERSTART_VALUE).toString();
            NettyHttpServer nettyHttpServer = new NettyHttpServer(HTTP_PORT,STATIC_LOCATION);
            logger.info("start grpc server on port: {}, grpc web proxy server on port: {}, " +
                    "http server server on port: {}", GRPC_PORT,GRPC_WEB_PROXY_PORT,HTTP_PORT);
            System.out.println("Open your web browser and navigate to " + "http" + "://127.0.0.1:" + HTTP_PORT + '/' + "index.html");
            nettyHttpServer.start();
        }
}
