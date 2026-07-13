public class netty_0061 {

    @Override
    public boolean distribute(int maxBytes, Writer writer) throws Http2Exception {
        if (connectionState.activeCountForTree == 0) {
            return false;
        }
        maxBytes = distributeUntilDone(maxBytes, writer);
        return connectionState.activeCountForTree != 0;
    }

    private int distributeUntilDone(int maxBytes, Writer writer) throws Http2Exception {
        int oldIsActiveCountForTree;
        do {
            oldIsActiveCountForTree = connectionState.activeCountForTree;
            maxBytes -= distributeToChildren(maxBytes, writer, connectionState);
        } while (connectionState.activeCountForTree != 0 &&
                (maxBytes > 0 || oldIsActiveCountForTree != connectionState.activeCountForTree));
        return maxBytes;
    }
}
