public class netty_0065 {

static void setSessionTicketKeys(SslSessionTicketKey @Nullable [] keys){
            if (keys != null && keys.length != 0) {
                byte[][] sessionKeys = new byte[keys.length][];
                for (int i = 0; i < keys.length; ++i) {
                    SslSessionTicketKey key = keys[i];
                    byte[] binaryKey = new byte[49];
                    // We mark the first key as preferred by using 1 as byte marker
                    binaryKey[0] = i == 0 ? (byte) 1 : (byte) 0;
                    int dstCurPos = 1;
                    System.arraycopy(key.name, 0, binaryKey, dstCurPos, 16);
                    dstCurPos += 16;
                    System.arraycopy(key.hmacKey, 0, binaryKey, dstCurPos, 16);
                    dstCurPos += 16;
                    System.arraycopy(key.aesKey, 0, binaryKey, dstCurPos, 16);
                    sessionKeys[i] = binaryKey;
                }
                this.sessionKeys = sessionKeys;
            } else {
                sessionKeys = null;
            }
        }
}
