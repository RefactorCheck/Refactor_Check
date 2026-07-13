public class nacos_0118 {

        @Override
        protected String printConfig() {
            PipelineConfig config = this.currentConfig;
            if (config == null) {
                return "PipelineConfig{null}";
            }
            StringBuilder sb = new StringBuilder("PipelineConfig{enabled=");
            sb.append(config.isEnabled());
            sb.append(", nodes=[");
            appendNodes(sb, config.getNodes());
            sb.append("]}");
            return sb.toString();
        }

        private void appendNodes(StringBuilder sb, List<PipelineNodeConfig> nodes) {
            if (nodes != null) {
                for (int i = 0; i < nodes.size(); i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append(nodes.get(i).getPipelineId());
                }
            }
        }
}
