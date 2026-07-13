public class nacos_0220 {


        public static String buildRefactored(PromptVersionInfo info) {
            if (info == null) {
                throw new IllegalArgumentException("PromptVersionInfo must not be null");
            }
            StringBuilder sb = new StringBuilder(512);
            
            // YAML frontmatter
            sb.append(YAML_DELIMITER).append(NEW_LINE);
            appendYamlScalar(sb, "promptKey", info.getPromptKey());
            appendYamlScalar(sb, "version", info.getVersion());
            appendYamlScalar(sb, "status", info.getStatus());
            appendYamlScalar(sb, "srcUser", info.getSrcUser());
            appendYamlScalar(sb, "md5", info.getMd5());
            if (info.getGmtModified() != null) {
                sb.append("gmtModified: ").append(info.getGmtModified()).append(NEW_LINE);
            }
            appendVariablesYaml(sb, info.getVariables());
            sb.append(YAML_DELIMITER).append(NEW_LINE).append(NEW_LINE);
            
            // Title
            sb.append("# ").append(safe(info.getPromptKey()));
            if (StringUtils.isNotBlank(info.getVersion())) {
                sb.append(" @ ").append(info.getVersion());
            }
            sb.append(NEW_LINE).append(NEW_LINE);
            
            // Commit message
            if (StringUtils.isNotBlank(info.getCommitMsg())) {
                sb.append("> ").append(info.getCommitMsg().replace(NEW_LINE, " ")).append(NEW_LINE)
                    .append(NEW_LINE);
            }
            
            // Variables section (human readable)
            if (info.getVariables() != null && !info.getVariables().isEmpty()) {
                sb.append("## Variables").append(NEW_LINE).append(NEW_LINE);
                sb.append("| Name | Default | Description |").append(NEW_LINE);
                sb.append("| --- | --- | --- |").append(NEW_LINE);
                for (PromptVariable v : info.getVariables()) {
                    sb.append("| ").append(mdCell(v.getName()))
                        .append(" | ").append(mdCell(v.getDefaultValue()))
                        .append(" | ").append(mdCell(v.getDescription()))
                        .append(" |").append(NEW_LINE);
                }
                sb.append(NEW_LINE);
            }
            
            // Template body
            sb.append("## Template").append(NEW_LINE).append(NEW_LINE);
            sb.append(FENCE).append(NEW_LINE);
            sb.append(info.getTemplate() == null ? "" : info.getTemplate());
            if (info.getTemplate() != null && !info.getTemplate().endsWith(NEW_LINE)) {
                sb.append(NEW_LINE);
            }
            sb.append(FENCE).append(NEW_LINE);
            
            return sb.toString();
        
        }
}
