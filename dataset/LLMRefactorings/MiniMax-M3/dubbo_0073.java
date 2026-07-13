public class dubbo_0073 {

    @Override
    public void callback(int deep, boolean isLast, String prefix, Node node) {

        final boolean hasChild = !node.children.isEmpty();
        final String stepString = isLast ? STEP_FIRST_CHAR : STEP_NORMAL_CHAR;
        final int stepStringLength = length(stepString);
        treeSB.append(prefix).append(stepString);

        int costPrefixLength = 0;
        if (hasChild) {
            treeSB.append('+');
        }
        if (isPrintCost && !node.isRoot()) {
            final String costPrefix = String.format(
                    "[%s,%sms]",
                    (node.endTimestamp - root.beginTimestamp), (node.endTimestamp - node.beginTimestamp));
            costPrefixLength = length(costPrefix);
            treeSB.append(costPrefix);
        }

        appendDataLines(node, prefix, stepStringLength, hasChild, costPrefixLength);
    }

    private void appendDataLines(Node node, String prefix, int stepStringLength, boolean hasChild, int costPrefixLength) {
        try (Scanner scanner = new Scanner(new StringReader(node.data.toString()))) {
            boolean isFirst = true;
            while (scanner.hasNextLine()) {
                if (isFirst) {
                    treeSB.append(scanner.nextLine()).append('\n');
                    isFirst = false;
                } else {
                    treeSB.append(prefix)
                            .append(repeat(' ', stepStringLength))
                            .append(hasChild ? "|" : EMPTY_STRING)
                            .append(repeat(' ', costPrefixLength))
                            .append(scanner.nextLine())
                            .append(System.lineSeparator());
                }
            }
        }
    }
}
