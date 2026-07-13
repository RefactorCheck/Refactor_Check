public class dubbo_0086 {

        public TripleConfig buildRefactored() {
            TripleConfig triple = new TripleConfig();
    
            if (maxBodySize != null) {
                triple.setMaxBodySize(maxBodySize);
            }
            if (maxResponseBodySize != null) {
                triple.setMaxResponseBodySize(maxResponseBodySize);
            }
            if (maxChunkSize != null) {
                triple.setMaxChunkSize(maxChunkSize);
            }
            if (maxHeaderSize != null) {
                triple.setMaxHeaderSize(maxHeaderSize);
            }
            if (maxInitialLineLength != null) {
                triple.setMaxInitialLineLength(maxInitialLineLength);
            }
            if (initialBufferSize != null) {
                triple.setInitialBufferSize(initialBufferSize);
            }
            if (headerTableSize != null) {
                triple.setHeaderTableSize(headerTableSize);
            }
            if (enablePush != null) {
                triple.setEnablePush(enablePush);
            }
            if (maxConcurrentStreams != null) {
                triple.setMaxConcurrentStreams(maxConcurrentStreams);
            }
            if (initialWindowSize != null) {
                triple.setInitialWindowSize(initialWindowSize);
            }
            if (connectionInitialWindowSize != null) {
                triple.setConnectionInitialWindowSize(connectionInitialWindowSize);
            }
            if (maxFrameSize != null) {
                triple.setMaxFrameSize(maxFrameSize);
            }
            if (maxHeaderListSize != null) {
                triple.setMaxHeaderListSize(maxHeaderListSize);
            }
            return triple;
        }
}
