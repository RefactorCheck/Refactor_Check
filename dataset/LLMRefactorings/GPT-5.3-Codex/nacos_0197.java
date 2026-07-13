public class nacos_0197 {


        @Override
        public AiResourceImportValidationItem validateRefactored(String namespaceId, AiResourceImportArtifact artifact, boolean overwriteExisting) throws NacosException {
            Skill skill = parseSkill(namespaceId, artifact);
            AiResource meta = resourceManager.findMeta(namespaceId, skill.getName(), resourceType());
            AiResourceImportValidationItem result = baseValidationItem(artifact, skill);
            result.setExists(meta != null);
            if (meta == null) {
                result.setStatus(AiResourceImportValidationStatus.VALID);
                return result;
            }
            ResourceVersionInfo info = AiResourceManager.requireVersionInfo(meta);
            if (hasReviewingVersion(info)) {
                result.setStatus(AiResourceImportValidationStatus.CONFLICT);
                result.setConflictType(CONFLICT_TYPE_REVIEWING_VERSION);
                result.setErrors(Collections.singletonList(
                    "There is already a reviewing version, cannot import until review finishes."));
                return result;
            }
            if (hasEditingVersion(info) && !overwriteExisting) {
                result.setStatus(AiResourceImportValidationStatus.CONFLICT);
                result.setConflictType(CONFLICT_TYPE_WORKING_VERSION);
                result.setErrors(Collections.singletonList(
                    "There is already an editing draft, enable overwrite to import."));
                return result;
            }
            result.setStatus(AiResourceImportValidationStatus.WARNING);
            result.setConflictType(CONFLICT_TYPE_EXISTING);
            result.setWarnings(Collections.singletonList(resolveExistingWarning(info)));
            return result;
        
        }
}
