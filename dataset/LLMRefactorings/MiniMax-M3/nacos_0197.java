public class nacos_0197 {

    @Override
    public AiResourceImportValidationItem validate(String namespaceId,
        AiResourceImportArtifact artifact, boolean overwriteExisting) throws NacosException {
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
            return markConflict(result, CONFLICT_TYPE_REVIEWING_VERSION,
                "There is already a reviewing version, cannot import until review finishes.");
        }
        if (hasEditingVersion(info) && !overwriteExisting) {
            return markConflict(result, CONFLICT_TYPE_WORKING_VERSION,
                "There is already an editing draft, enable overwrite to import.");
        }
        result.setStatus(AiResourceImportValidationStatus.WARNING);
        result.setConflictType(CONFLICT_TYPE_EXISTING);
        result.setWarnings(Collections.singletonList(resolveExistingWarning(info)));
        return result;
    }

    private AiResourceImportValidationItem markConflict(AiResourceImportValidationItem result,
        String conflictType, String message) {
        result.setStatus(AiResourceImportValidationStatus.CONFLICT);
        result.setConflictType(conflictType);
        result.setErrors(Collections.singletonList(message));
        return result;
    }
}
