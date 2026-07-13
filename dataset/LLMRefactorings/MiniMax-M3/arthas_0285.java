public class arthas_0285 {

        @Override
        public boolean isEmpty() {
            final Segment<K, V>[] segments = this.segments;
            /*
             * We keep track of per-segment modCounts to avoid ABA problems in which
             * an element in one segment was added and in another removed during
             * traversal, in which case the table was never actually empty at any
             * point. Note the similar use of modCounts in the size() and
             * containsValue() methods, which are the only other methods also
             * susceptible to ABA problems.
             */
            int[] mc = new int[segments.length];
            int mcsum = 0;
            for (int i = 0; i < segments.length; ++ i) {
                if (segments[i].count != 0) {
                    return false;
                } else {
                    mcsum += mc[i] = segments[i].modCount;
                }
            }
            // If mcsum happens to be zero, then we know we got a snapshot before
            // any modifications at all were made.  This is probably common enough
            // to bother tracking.
            if (mcsum != 0) {
                return snapshotStillValid(segments, mc);
            }
            return true;
        }

        private boolean snapshotStillValid(Segment<K, V>[] segments, int[] mc) {
            for (int i = 0; i < segments.length; ++ i) {
                if (segments[i].count != 0 || mc[i] != segments[i].modCount) {
                    return false;
                }
            }
            return true;
        }
}
