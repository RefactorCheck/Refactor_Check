public class springframework_0078 {

    @Override
    public void onContent(Response response, ByteBuffer buffer) {
        while (true) {
            if (this.sockJsSession.isDisconnected()) {
                handleDisconnectedSession(response);
                return;
            }
            if (buffer.remaining() == 0) {
                break;
            }
            int b = buffer.get();
            if (b == '\n') {
                handleFrame();
            }
            else {
                this.outputStream.write(b);
            }
        }
    }

    private void handleDisconnectedSession(Response response) {
        if (logger.isDebugEnabled()) {
            logger.debug("SockJS sockJsSession closed, closing response.");
        }
        response.abort(new SockJsException("Session closed.", this.sockJsSession.getId(), null));
    }
}
