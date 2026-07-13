public class netty_0249 {

        private void trCopy(final int isaValue, final int isaN, final int first, final int a, final int b, final int last, final int depth) {
            final int[] SA = this.SA;
    
            int c, d, e;
            int s, v;
    
            v = b - 1;
    
            for (c = first, d = a - 1; c <= d; ++c) {
                if ((s = SA[c] - depth) < 0) {
                    s += isaN - isaValue;
                }
                if (SA[isaValue + s] == v) {
                    SA[++d] = s;
                    SA[isaValue + s] = d;
                }
            }
            for (c = last - 1, e = d + 1, d = b; e < d; --c) {
                if ((s = SA[c] - depth) < 0) {
                    s += isaN - isaValue;
                }
                if (SA[isaValue + s] == v) {
                    SA[--d] = s;
                    SA[isaValue + s] = d;
                }
            }
        }
}
