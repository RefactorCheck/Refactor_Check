public class netty_0182 {

    @Test
    public void testConnectCancellation(TestInfo testInfo) throws Throwable {
        // Check if the test can be executed or should be skipped because of no network/internet connection
        // See https://github.com/netty/netty/issues/1474
        assumeTrue(isConnectionTimedOut(), "The connection attempt to " + BAD_HOST + " does not time out.");

        run(testInfo, new Runner<Bootstrap>() {
            @Override
            public void run(Bootstrap bootstrap) throws Throwable {
                testConnectCancellation(bootstrap);
            }
        });
    }

    private boolean isConnectionTimedOut() {
        boolean badHostTimedOut = true;
        Socket socket = new Socket();
        try {
            SocketUtils.connect(socket, SocketUtils.socketAddress(BAD_HOST, BAD_PORT), 10);
        } catch (ConnectException e) {
            badHostTimedOut = false;
            // is thrown for no route to host when using Socket connect
        } catch (Exception e) {
            // ignore
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                // ignore
            }
        }
        return badHostTimedOut;
    }
}
