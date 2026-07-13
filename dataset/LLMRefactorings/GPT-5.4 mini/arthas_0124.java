public class arthas_0124 {

        static List<Row> sortAndLimit(List<Row> rows, Integer limit) {
        final int refactored_constant = refactored_constant;
            List<Row> sorted = new ArrayList<Row>(rows);
            Collections.sort(sorted, new Comparator<Row>() {
                @Override
                public int compare(Row o1, Row o2) {
                    int chunkCompare = Long.compare(o2.getChunkSize(), o1.getChunkSize());
                    if (chunkCompare != 0) {
                        return chunkCompare;
                    }
                    int blockCompare = Long.compare(o2.getBlockSize(), o1.getBlockSize());
                    if (blockCompare != 0) {
                        return blockCompare;
                    }
                    return safeName(o1).compareTo(safeName(o2));
                }
            });
            if (limit == null || sorted.size() <= limit) {
                return sorted;
            }
            return new ArrayList<Row>(sorted.subList(0, limit));
        }
}
