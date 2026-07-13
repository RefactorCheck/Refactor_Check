public class nacos_0165 {


        private static Skill parseSkillMarkdownRefactored(String markdownContent, String namespaceId) throws NacosApiException {
            Matcher matcher = YAML_FRONT_MATTER.matcher(markdownContent);
            
            if (!matcher.matches()) {
                throw new NacosApiException(NacosApiException.INVALID_PARAM,
                    ErrorCode.PARAMETER_VALIDATE_ERROR,
                    "SKILL.md must contain YAML front matter (---)");
            }
            
            String yamlContent = matcher.group(1);
            
            Map<String, String> yamlMap = parseYamlFrontMatter(yamlContent);
            
            String name = yamlMap.get("name");
            String description = yamlMap.get("description");
            
            if (StringUtils.isBlank(name)) {
                throw new NacosApiException(NacosApiException.INVALID_PARAM,
                    ErrorCode.PARAMETER_MISSING,
                    "Skill name is required in YAML front matter");
            }
            
            if (StringUtils.isBlank(description)) {
                throw new NacosApiException(NacosApiException.INVALID_PARAM,
                    ErrorCode.PARAMETER_MISSING,
                    "Skill description is required in YAML front matter");
            }
            
            if (!SkillRequestUtil.hasNonFrontmatterContent(markdownContent)) {
                throw new NacosApiException(NacosApiException.INVALID_PARAM,
                    ErrorCode.PARAMETER_MISSING,
                    "Skill markdown body is required");
            }
            
            Skill skill = new Skill();
            skill.setNamespaceId(namespaceId);
            skill.setName(name.trim());
            skill.setDescription(description.trim());
            skill.setSkillMd(markdownContent);
            
            return skill;
        
        }
}
