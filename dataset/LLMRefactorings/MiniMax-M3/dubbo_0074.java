public class dubbo_0074 {

        public synchronized E randomSelectOne() {
            int originSize = originList.size();
            int tailSize = tailList != null ? tailList.size() : 0;
            int totalSize = originSize + tailSize;
            int cardinality = rootSet.cardinality();
    
            // example 1 : origin size is 1000, cardinality is 50, rate is 1/20. 20 * 2 = 40 < 50, try random select
            // example 2 : origin size is 1000, cardinality is 25, rate is 1/40. 40 * 2 = 80 > 50, directly use iterator
            int rate = originSize / cardinality;
            if (rate <= cardinality * 2) {
                int count = rate * 5;
                E result = tryRandomSelect(originSize, totalSize, count);
                if (result != null) {
                    return result;
                }
            }
            return get(ThreadLocalRandom.current().nextInt(cardinality + tailSize));
        }
    
        private E tryRandomSelect(int originSize, int totalSize, int count) {
            for (int i = 0; i < count; i++) {
                int random = ThreadLocalRandom.current().nextInt(totalSize);
                if (random < originSize) {
                    if (rootSet.get(random)) {
                        return originList.get(random);
                    }
                } else {
                    return tailList.get(random - originSize);
                }
            }
            return null;
        }
}
