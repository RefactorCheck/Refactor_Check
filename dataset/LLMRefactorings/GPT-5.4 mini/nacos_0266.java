public class nacos_0266 {

        private String overwriteUploadedSkillRefactored(String namespaceId, Skill skill, String uploadVersion,
            AiResource meta, boolean requireEditingDraft, String commitMsg)
            throws NacosException {
            String name = skill.getName();
            // No meta record = brand-new skill, create directly
            if (meta == null) {
                if (requireEditingDraft) {
                    throw new NacosApiException(NacosException.CONFLICT, ErrorCode.RESOURCE_CONFLICT,
                        "No editing draft to overwrite: " + name);
                }
                createDraftWithSkill(namespaceId, skill, uploadVersion, null, true, commitMsg);
                return name;
            }
            
            VisibilityHelper.checkWritableResource(meta);
            ResourceVersionInfo info = AiResourceManager.requireVersionInfo(meta);
            ensureNoReviewingVersion(info, "overwrite upload");
            String editing = info.getEditingVersion();
            if (StringUtils.isNotBlank(editing)) {
                String targetVersion = resolveOverwriteDraftVersion(namespaceId, name, uploadVersion,
                    editing);
                if (editing.equals(targetVersion)) {
                    overwriteEditingDraft(namespaceId, skill, meta, editing, commitMsg);
                    return name;
                }
                deleteDraftAndCreateUploadedSkill(namespaceId, skill, targetVersion, meta, commitMsg);
                return name;
            }
            
            if (requireEditingDraft) {
                throw new NacosApiException(NacosException.CONFLICT, ErrorCode.RESOURCE_CONFLICT,
                    "No editing draft to overwrite: " + name);
            }
            
            // No editing draft: assign new version number and create new draft
            String newVersion = resolveFinalUploadVersion(namespaceId, name, uploadVersion);
            createDraftWithSkill(namespaceId, skill, newVersion, meta, false, commitMsg);
            return name;
        }
}
