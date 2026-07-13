public class arthas_0047 {

        private String renderCost(MethodNode node) {
            StringBuilder sb = new StringBuilder();
            if (node.getTimes() <= 1) {
                if(node.parent() instanceof ThreadNode) {
                    sb.append('[').append(nanoToMillis(node.getCost())).append(TIME_UNIT).append("] ");
                }else {
                    MethodNode parentNode = (MethodNode) node.parent();
                    String percentage = String.format("%.2f", node.getCost()*100.0/parentNode.getTotalCost());
                    sb.append('[').append(percentage).append(PERCENTAGE).append(" ").append(nanoToMillis(node.getCost())).append(TIME_UNIT).append(" ").append("] ");
    
                }
            } else {
                if(node.parent() instanceof ThreadNode) {
                    sb.append("[min=").append(nanoToMillis(node.getMinCost())).append(TIME_UNIT).append(",max=")
                            .append(nanoToMillis(node.getMaxCost())).append(TIME_UNIT).append(",total=")
                            .append(nanoToMillis(node.getTotalCost())).append(TIME_UNIT).append(",count=")
                            .append(node.getTimes()).append("] ");
                }else {
                    MethodNode parentNode = (MethodNode) node.parent();
                    String percentage = String.format("%.2f",node.getTotalCost()*100.0/parentNode.getTotalCost());
                    sb.append('[').append(percentage).append(PERCENTAGE).append(" min=").append(nanoToMillis(node.getMinCost())).append(TIME_UNIT).append(",max=")
                            .append(nanoToMillis(node.getMaxCost())).append(TIME_UNIT).append(",total=")
                            .append(nanoToMillis(node.getTotalCost())).append(TIME_UNIT).append(",count=")
                            .append(node.getTimes()).append("] ");
                }
    
            }
            return sb.toString();
        }
}
