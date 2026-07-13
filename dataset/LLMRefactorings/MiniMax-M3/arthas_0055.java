public class arthas_0055 {

    public void serverStart() throws IOException, InterruptedException {
        ComplexObject complexObject = createComplexObject();
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
        Thread grpcWebProxyStartThread = new Thread(() -> {
            GrpcWebProxyServer grpcWebProxyServer = new GrpcWebProxyServer(GRPC_WEB_PROXY_PORT,GRPC_PORT);
            grpcWebProxyServer.start();
        });
        grpcWebProxyStartThread.start();
        String staticLocation = getStaticLocation();
        NettyHttpServer nettyHttpServer = new NettyHttpServer(HTTP_PORT, staticLocation);
        logger.info("start grpc server on port: {}, grpc web proxy server on port: {}, " +
                "http server server on port: {}", GRPC_PORT,GRPC_WEB_PROXY_PORT,HTTP_PORT);
        System.out.println("Open your web browser and navigate to " + "http" + "://127.0.0.1:" + HTTP_PORT + '/' + "index.html");
        nettyHttpServer.start();
    }

    private String getStaticLocation() {
        String currentDir = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile().getPath();
        return Paths.get(currentDir, "static").toString();
    }
}
