public class netty_0257 {

    private void shiftAndFindBoundary(final int[] SA, final int first, int b) {
        do {
            SA[b + 1] = SA[b];
        } while (first <= --b && SA[b] < 0);
    }

    private void trInsertionSort(final int isa, final int isaD, final int isaN, int first, int last) {
        final int[] SA = this.SA;

        int a, b;
        int t, r;

        for (a = first + 1; a < last; ++a) {
            for (t = SA[a], b = a - 1; 0 > (r = trGetC(isa, isaD, isaN, t) - trGetC(isa, isaD, isaN, SA[b]));) {
                shiftAndFindBoundary(SA, first, b);
                if (b < first) {
                    break;
                }
            }
            if (r == 0) {
                SA[b] = ~SA[b];
            }
            SA[b + 1] = t;
        }
    }
}
