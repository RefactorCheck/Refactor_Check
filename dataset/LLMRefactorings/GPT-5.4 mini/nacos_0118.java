public class nacos_0118 {

        @Override
        protected String printConfigRefactored() {
            PipelineConfig config = this.currentConfig;
            if (config == null) {
                return "PipelineConfig{null}";
            }
            StringBuilder sb = new StringBuilder("PipelineConfig{enabled=");
            sb.append(config.isEnabled());
            sb.append(", nodes=[");
            List<PipelineNodeConfig> nodes = config.getNodes();
            if (nodes != null) {
                for (int i = 0; i < nodes.size(); i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append(nodes.get(i).getPipelineId());
                }
            }
            sb.append("]}");
            return sb.toString();
        }
}
