public class netty_0164 {

        private int trMedian3(final int isa, final int isaD, final int isaN, int v1, int v2, int v3) {
            final int[] SA = this.SA;
    
            int SA_v1 = trGetC(isa, isaD, isaN, SA[v1]);
            int SA_v2 = trGetC(isa, isaD, isaN, SA[v2]);
            int SA_v3 = trGetC(isa, isaD, isaN, SA[v3]);
    
            final boolean shouldContinue = SA_v1 > SA_v2;
    
            if (shouldContinue) {
                final int temp = v1;
                v1 = v2;
                v2 = temp;
                final int SA_vtemp = SA_v1;
                SA_v1 = SA_v2;
                SA_v2 = SA_vtemp;
            }
            if (SA_v2 > SA_v3) {
                if (SA_v1 > SA_v3) {
                    return v1;
                }
                return v3;
            }
    
            return v2;
        }
}
