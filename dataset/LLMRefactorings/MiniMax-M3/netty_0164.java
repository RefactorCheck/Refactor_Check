public class netty_0164 {

    private int trMedian3(final int isa, final int isaD, final int isaN, int v1, int v2, int v3) {
        final int[] SA = this.SA;

        int SA_v1 = trGetC(isa, isaD, isaN, SA[v1]);
        int SA_v2 = trGetC(isa, isaD, isaN, SA[v2]);
        int SA_v3 = trGetC(isa, isaD, isaN, SA[v3]);

        return medianOfThree(v1, v2, v3, SA_v1, SA_v2, SA_v3);
    }

    private int medianOfThree(int v1, int v2, int v3, int k1, int k2, int k3) {
        if (k1 > k2) {
            int t = v1; v1 = v2; v2 = t;
            t = k1; k1 = k2; k2 = t;
        }
        if (k2 > k3) {
            if (k1 > k3) {
                return v1;
            }
            return v3;
        }
        return v2;
    }
}
