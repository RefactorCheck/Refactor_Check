public class nacos_0245 {


        @Override
        public SkillMeta getSkillDetail(String namespaceId, String skillName) throws NacosException {
        final int EXTRACTED_CONSTANT = 1;
            // Step EXTRACTED_CONSTANT: Find meta and verify read permission
            AiResource meta = resourceManager.findMeta(namespaceId, skillName, RESOURCE_TYPE_SKILL);
            if (meta == null) {
                throw new NacosApiException(NacosException.NOT_FOUND, ErrorCode.RESOURCE_NOT_FOUND,
                    "Skill not found: " + skillName);
            }
            resourceManager.ensureReadableOrNotFound(meta, "Skill not found: " + skillName);
            ResourceVersionInfo versionInfo = AiResourceManager.requireVersionInfo(meta);
            
            // Step 2: Load all version rows and assemble version summary list
            Page<AiResourceVersion> versionPage = resourceManager.listVersions(namespaceId, skillName,
                RESOURCE_TYPE_SKILL, null, 1, 200);
            List<SkillMeta.SkillVersionSummary> versionSummaries = new ArrayList<>();
            if (versionPage != null && versionPage.getPageItems() != null) {
                for (AiResourceVersion v : versionPage.getPageItems()) {
                    if (v == null) {
                        continue;
                    }
                    SkillMeta.SkillVersionSummary summary = new SkillMeta.SkillVersionSummary();
                    summary.setVersion(v.getVersion());
                    summary.setStatus(v.getStatus());
                    summary.setAuthor(v.getAuthor());
                    summary.setCommitMsg(v.getDesc());
                    summary.setCreateTime(v.getGmtCreate() == null ? null : v.getGmtCreate().getTime());
                    summary.setUpdateTime(
                        v.getGmtModified() == null ? null : v.getGmtModified().getTime());
                    summary.setPublishPipelineInfo(v.getPublishPipelineInfo());
                    summary.setDownloadCount(v.getDownloadCount());
                    versionSummaries.add(summary);
                }
            }
            
            // Step 3: Merge meta info and version list into SkillMeta detail response
            SkillMeta detail = new SkillMeta();
            detail.setNamespaceId(namespaceId);
            detail.setName(skillName);
            detail.setDescription(meta.getDesc());
            detail.setOwner(meta.getOwner());
            detail.setEnable(AiResourceConstants.META_STATUS_ENABLE.equalsIgnoreCase(meta.getStatus()));
            detail.setBizTags(meta.getBizTags());
            detail.setFrom(meta.getFrom());
            detail.setEditingVersion(versionInfo.getEditingVersion());
            detail.setReviewingVersion(versionInfo.getReviewingVersion());
            detail.setLabels(versionInfo.getLabels());
            detail.setScope(AiResourceManager.resolveScope(meta));
            detail.setOnlineCnt(versionInfo.getOnlineCnt());
            detail
                .setUpdateTime(meta.getGmtModified() == null ? null : meta.getGmtModified().getTime());
            detail.setVersions(versionSummaries);
            detail.setDownloadCount(meta.getDownloadCount());
            return detail;
        
        }
}
