public class netty_0186 {

        private void rehash(int newCapacity) {
            long[] oldTokens = tokens;
            int[] oldRegistrationIds = registrationIds;
            byte[] oldOps = ops;
            long[] oldUserDatas = userDatas;
    
            tokens = new long[newCapacity];
            registrationIds = new int[newCapacity];
            ops = new byte[newCapacity];
            userDatas = new long[newCapacity];
            mask = newCapacity - 1;
            maxSize = calcMaxSize(newCapacity);
            size = 0;
            tombstones = 0;
    
            transfer(oldTokens, oldRegistrationIds, oldOps, oldUserDatas);
        }

        private void transfer(long[] oldTokens, int[] oldRegistrationIds, byte[] oldOps, long[] oldUserDatas) {
            for (int i = 0; i < oldTokens.length; i++) {
                long token = oldTokens[i];
                if (token < 0) {
                    insertDuringRehash(token, oldRegistrationIds[i], oldOps[i], oldUserDatas[i]);
                }
            }
        }
}
