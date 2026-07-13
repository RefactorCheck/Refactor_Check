public class netty_0061 {

        @Override
        public boolean distribute(int maxBytes, Writer writer) throws Http2Exception {
            // As long as there is some active frame we should write at least 1 time.
            if (connectionState.activeCountForTree == 0) {
                return false;
            }
    
            // The goal is to write until we write all the allocated bytes or are no longer making progress.
            // We still attempt to write even after the number of allocated bytes has been exhausted to allow empty frames
            // to be sent. Making progress means the active streams rooted at the connection stream has changed.
            int oldIsActiveCountForTree;
            do {
                oldIsActiveCountForTree = connectionState.activeCountForTree;
                // connectionState will never be active, so go right to its children.
                maxBytes -= distributeToChildren(maxBytes, writer, connectionState);
            } while (connectionState.activeCountForTree != 0 &&
                    (maxBytes > 0 || oldIsActiveCountForTree != connectionState.activeCountForTree));
    
            return connectionState.activeCountForTree != 0;
        }
}
