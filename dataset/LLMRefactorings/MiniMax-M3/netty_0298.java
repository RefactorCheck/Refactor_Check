public class netty_0298 {

        private static void orderedCopyOnInsert(DefaultAttribute[] sortedSrc, int srcLength, DefaultAttribute[] copy,
                                                DefaultAttribute toInsert) {
            // let's walk backward, because as a rule of thumb, toInsert.key.id() tends to be higher for new keys
            final int id = toInsert.key.id();
            int i = findInsertionPoint(sortedSrc, srcLength, copy, id);
            copy[i + 1] = toInsert;
            final int toCopy = i + 1;
            if (toCopy > 0) {
                System.arraycopy(sortedSrc, 0, copy, 0, toCopy);
            }
        }

        private static int findInsertionPoint(DefaultAttribute[] sortedSrc, int srcLength, DefaultAttribute[] copy, int id) {
            int i;
            for (i = srcLength - 1; i >= 0; i--) {
                DefaultAttribute attribute = sortedSrc[i];
                int attributeKeyId = attribute.key.id();
                assert attributeKeyId != id;
                if (attributeKeyId < id) {
                    break;
                }
                copy[i + 1] = attribute;
            }
            return i;
        }
}
