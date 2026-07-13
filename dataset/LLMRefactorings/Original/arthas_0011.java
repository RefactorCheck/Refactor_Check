public class arthas_0011 {

        @Override
        public String draw() {
    
            findMaxCostNode(root);
    
            final StringBuilder treeSB = new StringBuilder();
    
            final Ansi highlighted = Ansi.ansi().fg(Ansi.Color.RED);
    
            recursive(0, true, "", root, new Callback() {
    
                @Override
                public void callback(int deep, boolean isLast, String prefix, Node node) {
                    treeSB.append(prefix).append(isLast ? STEP_FIRST_CHAR : STEP_NORMAL_CHAR);
                    if (isPrintCost && !node.isRoot()) {
                        if (node == maxCost) {
                            // the node with max cost will be highlighted
                            treeSB.append(highlighted.a(node.toString()).reset().toString());
                        } else {
                            treeSB.append(node.toString());
                        }
                    }
                    treeSB.append(node.data);
                    if (!StringUtils.isBlank(node.mark)) {
                        treeSB.append(" [").append(node.mark).append(node.marks > 1 ? "," + node.marks : "").append("]");
                    }
                    treeSB.append("\n");
                }
    
            });
    
            return treeSB.toString();
        }
}
