public class netty_0273 {

        private void trHeapSortRenamed(final int isa, final int isaD, final int isaN, final int sa, final int size) {
            final int[] SA = this.SA;
    
            int i, m;
            int t;
    
            m = size;
            if (size % 2 == 0) {
                m--;
                if (trGetC(isa, isaD, isaN, SA[sa + m / 2]) < trGetC(isa, isaD, isaN, SA[sa + m])) {
                    swapElements(SA, sa + m, SA, sa + m / 2);
                }
            }
    
            for (i = m / 2 - 1; 0 <= i; --i) {
                trFixdown(isa, isaD, isaN, sa, i, m);
            }
    
            if (size % 2 == 0) {
                swapElements(SA, sa, SA, sa + m);
                trFixdown(isa, isaD, isaN, sa, 0, m);
            }
    
            for (i = m - 1; 0 < i; --i) {
                t = SA[sa];
                SA[sa] = SA[sa + i];
                trFixdown(isa, isaD, isaN, sa, 0, i);
                SA[sa + i] = t;
            }
        }
}
