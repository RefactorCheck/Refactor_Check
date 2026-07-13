public class nacos_0265 {

        private void createDraftWithSkillRefactored(String namespaceId, Skill skill, String version,
            AiResource existedMeta,
            boolean isNewSkill, String commitMsg) throws NacosException {
            String skillName = skill.getName();
            String currentUser = VisibilityHelper.resolveCurrentIdentity();
            
            // Normalize frontmatter before writing to storage
            SkillRequestUtil.normalizeSkillFrontmatter(skill, skillName, version, existedMeta == null);
            
            // 1) write all resources (including SKILL.md) to storage
            List<String> files = writeSkillToStorage(namespaceId, skill, version);
            
            // 2) insert draft version row
            String versionDesc = StringUtils.isNotBlank(commitMsg) ? commitMsg : "";
            resourceManager.insertVersionRow(namespaceId, skillName, RESOURCE_TYPE_SKILL,
                StringUtils.isBlank(currentUser) ? DEFAULT_AUTHOR : currentUser,
                AiResourceConstants.VERSION_STATUS_DRAFT, version, versionDesc,
                buildStorageJson(namespaceId, skillName, version, files,
                    SkillContentDigestUtils.computeContentMd5(skill)));
            
            // 3) create or update meta for editingVersion
            resourceManager.initOrUpdateMetaForDraft(namespaceId, skillName, RESOURCE_TYPE_SKILL,
                skill.getDescription(), null, version, existedMeta, isNewSkill);
        }
}
