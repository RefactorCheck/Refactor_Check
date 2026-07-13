public class arthas_0011 {

        @Override
        public String draw() {
    
            findMaxCostNode(root);
    
            final StringBuilder treeSB = new StringBuilder();
    
            final Ansi highlighted = Ansi.ansi().fg(Ansi.Color.RED);
    
            recursive(0, true, "", root, new Callback() {
    
                @Override
                public void callback(int deep, boolean isLast, String prefix, Node node) {
                    appendNodeLine(treeSB, prefix, isLast, node, highlighted);
                }
    
            });
    
            return treeSB.toString();
        }

        private void appendNodeLine(StringBuilder treeSB, String prefix, boolean isLast, Node node, Ansi highlighted) {
            treeSB.append(prefix).append(isLast ? STEP_FIRST_CHAR : STEP_NORMAL_CHAR);
            if (isPrintCost && !node.isRoot()) {
                if (node == maxCost) {
                    treeSB.append(highlighted.a(node.toString()).reset().toString());
                } else {
                    treeSB.append(node.toString());
                }
            }
            treeSB.append(node.data);
            appendNodeMark(treeSB, node);
            treeSB.append("\n");
        }

        private void appendNodeMark(StringBuilder treeSB, Node node) {
            if (!StringUtils.isBlank(node.mark)) {
                treeSB.append(" [").append(node.mark).append(node.marks > 1 ? "," + node.marks : "").append("]");
            }
        }
}
