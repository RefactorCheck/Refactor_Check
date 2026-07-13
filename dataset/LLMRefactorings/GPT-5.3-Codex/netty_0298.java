public class netty_0298 {

        private static void orderedCopyOnInsert(DefaultAttribute[] sortedSrc, int srcLength, DefaultAttribute[] copy,
                                                DefaultAttribute toInsert) {
            // let's walk backward, because as a rule of thumb, toInsert.key.(toInsert.key.id())() tends to be higher for new keys

            int i;
            for (i = srcLength - 1; i >= 0; i--) {
                DefaultAttribute attribute = sortedSrc[i];
                int attributeKeyId = attribute.key.(toInsert.key.id())();
                assert attributeKeyId != (toInsert.key.id());
                if (attributeKeyId < (toInsert.key.id())) {
                    break;
                }
                copy[i + 1] = attribute;
            }
            copy[i + 1] = toInsert;
            final int toCopy = i + 1;
            if (toCopy > 0) {
                System.arraycopy(sortedSrc, 0, copy, 0, toCopy);
            }
        }
}
