public class netty_0273 {

        private void trHeapSort(final int isa, final int isaD, final int isaN, final int sa, final int size) {
            int i, m;
            int t;
    
            m = size;
            if (size % 2 == 0) {
                m--;
                if (trGetC(isa, isaD, isaN, (this.SA)[sa + m / 2]) < trGetC(isa, isaD, isaN, (this.SA)[sa + m])) {
                    swapElements((this.SA), sa + m, (this.SA), sa + m / 2);
                }
            }
    
            for (i = m / 2 - 1; 0 <= i; --i) {
                trFixdown(isa, isaD, isaN, sa, i, m);
            }
    
            if (size % 2 == 0) {
                swapElements((this.SA), sa, (this.SA), sa + m);
                trFixdown(isa, isaD, isaN, sa, 0, m);
            }
    
            for (i = m - 1; 0 < i; --i) {
                t = (this.SA)[sa];
                (this.SA)[sa] = (this.SA)[sa + i];
                trFixdown(isa, isaD, isaN, sa, 0, i);
                (this.SA)[sa + i] = t;
            }
        }
}
