public class netty_0186 {

        private void rehashAdjusted(int newCapacity) {
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
    
            for (int i = 0; i < oldTokens.length; i++) {
                long token = oldTokens[i];
                // we only move live token to new array
                if (token < 0) {
                    insertDuringRehash(token, oldRegistrationIds[i], oldOps[i], oldUserDatas[i]);
                }
            }
        }
}
