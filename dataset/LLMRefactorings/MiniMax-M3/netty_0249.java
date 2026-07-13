public class netty_0249 {

        private int getShiftedIndex(int index, int depth, int isaN, int isa) {
            int s = index - depth;
            if (s < 0) {
                s += isaN - isa;
            }
            return s;
        }

        private void trCopy(final int isa, final int isaN, final int first,
                            final int a, final int b, final int last, final int depth) {
            final int[] SA = this.SA;
    
            int c, d, e;
            int s, v;
    
            v = b - 1;
    
            for (c = first, d = a - 1; c <= d; ++c) {
                s = getShiftedIndex(SA[c], depth, isaN, isa);
                if (SA[isa + s] == v) {
                    SA[++d] = s;
                    SA[isa + s] = d;
                }
            }
            for (c = last - 1, e = d + 1, d = b; e < d; --c) {
                s = getShiftedIndex(SA[c], depth, isaN, isa);
                if (SA[isa + s] == v) {
                    SA[--d] = s;
                    SA[isa + s] = d;
                }
            }
        }
}
