public class dubbo_0107 {

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder
                    .append(routerName)
                    .append(' ')
                    .append(" Total: ")
                    .append(total)
                    .append("\n");
    
            for (Map.Entry<String, BitList<Invoker<T>>> entry : grouping.entrySet()) {
                appendGrouping(stringBuilder, entry);
            }
            return stringBuilder.toString();
        }

        private void appendGrouping(StringBuilder stringBuilder, Map.Entry<String, BitList<Invoker<T>>> entry) {
            BitList<Invoker<T>> invokers = entry.getValue();
            stringBuilder
                    .append("[ ")
                    .append(entry.getKey())
                    .append(" -> ")
                    .append(
                            invokers.isEmpty()
                                    ? "Empty"
                                    : invokers.stream()
                                            .limit(5)
                                            .map(Invoker::getUrl)
                                            .map(URL::getAddress)
                                            .collect(Collectors.joining(",")))
                    .append(invokers.size() > 5 ? "..." : "")
                    .append(" (Total: ")
                    .append(invokers.size())
                    .append(") ]")
                    .append("\n");
        }
}
