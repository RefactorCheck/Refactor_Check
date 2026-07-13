public class netty_0065 {

        void setSessionTicketKeys(SslSessionTicketKey @Nullable [] keys) {
            if (keys != null && keys.length != 0) {
                byte[][] sessionKeys = new byte[keys.length][];
                for (int i = 0; i < keys.length; ++i) {
                    sessionKeys[i] = toBinaryKey(keys[i], i == 0);
                }
                this.sessionKeys = sessionKeys;
            } else {
                sessionKeys = null;
            }
        }

        private static byte[] toBinaryKey(SslSessionTicketKey key, boolean isPreferred) {
            byte[] binaryKey = new byte[49];
            binaryKey[0] = isPreferred ? (byte) 1 : (byte) 0;
            int dstCurPos = 1;
            System.arraycopy(key.name, 0, binaryKey, dstCurPos, 16);
            dstCurPos += 16;
            System.arraycopy(key.hmacKey, 0, binaryKey, dstCurPos, 16);
            dstCurPos += 16;
            System.arraycopy(key.aesKey, 0, binaryKey, dstCurPos, 16);
            return binaryKey;
        }
}
