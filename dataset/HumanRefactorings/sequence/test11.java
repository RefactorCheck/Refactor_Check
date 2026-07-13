public class test11{
    private int getGridY(int limit) {
        int previous = 0, res = 1;
        while (res < limit) {
            //extract here
            previous = res;
            res += 1;
            //to here
        }
        return previous;
    }
}
