public class arthas_0047 {

    private String renderCost(MethodNode node) {
        StringBuilder sb = new StringBuilder();
        if (node.getTimes() <= 1) {
            sb.append(renderSingleInvocation(node));
        } else {
            sb.append(renderMultipleInvocations(node));
        }
        return sb.toString();
    }

    private String renderSingleInvocation(MethodNode node) {
        if (node.parent() instanceof ThreadNode) {
            return "[" + nanoToMillis(node.getCost()) + TIME_UNIT + "] ";
        }
        MethodNode parentNode = (MethodNode) node.parent();
        String percentage = String.format("%.2f", node.getCost() * 100.0 / parentNode.getTotalCost());
        return "[" + percentage + PERCENTAGE + " " + nanoToMillis(node.getCost()) + TIME_UNIT + " " + "] ";
    }

    private String renderMultipleInvocations(MethodNode node) {
        if (node.parent() instanceof ThreadNode) {
            return "[min=" + nanoToMillis(node.getMinCost()) + TIME_UNIT + ",max="
                    + nanoToMillis(node.getMaxCost()) + TIME_UNIT + ",total="
                    + nanoToMillis(node.getTotalCost()) + TIME_UNIT + ",count="
                    + node.getTimes() + "] ";
        }
        MethodNode parentNode = (MethodNode) node.parent();
        String percentage = String.format("%.2f", node.getTotalCost() * 100.0 / parentNode.getTotalCost());
        return "[" + percentage + PERCENTAGE + " min=" + nanoToMillis(node.getMinCost()) + TIME_UNIT + ",max="
                + nanoToMillis(node.getMaxCost()) + TIME_UNIT + ",total="
                + nanoToMillis(node.getTotalCost()) + TIME_UNIT + ",count="
                + node.getTimes() + "] ";
    }
}
