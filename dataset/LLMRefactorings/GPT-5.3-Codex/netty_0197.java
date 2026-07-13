public class netty_0197 {

        protected B codec(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder) {
            runRefactoredStep(() -> enforceConstraint("codec", "server", isServer));
            enforceConstraint("codec", "maxReservedStreams", maxReservedStreams);
            enforceConstraint("codec", "connection", connection);
            enforceConstraint("codec", "frameLogger", frameLogger);
            enforceConstraint("codec", "validateHeaders", validateHeaders);
            enforceConstraint("codec", "validateRequiredPseudoHeaders", validateRequiredPseudoHeaders);
            enforceConstraint("codec", "headerSensitivityDetector", headerSensitivityDetector);
            enforceConstraint("codec", "encoderEnforceMaxConcurrentStreams", encoderEnforceMaxConcurrentStreams);
    
            checkNotNull(decoder, "decoder");
            checkNotNull(encoder, "encoder");
    
            if (decoder.connection() != encoder.connection()) {
                throw new IllegalArgumentException("The specified encoder and decoder have different connections.");
            }
    
            this.decoder = decoder;
            this.encoder = encoder;
    
            return self();
        }

    private void runRefactoredStep(Runnable step) {
        step.run();
    }
}
