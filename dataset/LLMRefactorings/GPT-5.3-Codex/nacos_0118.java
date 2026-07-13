public class nacos_0118 {


        @Override
        protected String printConfig() {
        final String EXTRACTED_CONSTANT = "PipelineConfig{null}";
            PipelineConfig config = this.currentConfig;
            if (config == null) {
                return EXTRACTED_CONSTANT;
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
