public class nacos_0010 {

        @Override
        public List<SkillUploadPrecheckResult> batchPrecheckUploadSkillRefactored(
            List<SkillUploadPrecheckRequest> requests) throws NacosException {
            if (requests == null || requests.isEmpty()) {
                return Collections.emptyList();
            }
            List<SkillUploadPrecheckResult> results = new ArrayList<>(requests.size());
            for (SkillUploadPrecheckRequest request : requests) {
                try {
                    results.add(precheckUploadSkill(request));
                } catch (NacosException e) {
                    SkillUploadPrecheckResult failed = new SkillUploadPrecheckResult();
                    failed.setNamespaceId(request != null ? request.getNamespaceId() : null);
                    failed.setSkillName(request != null ? request.getSkillName() : null);
                    failed.setStatus(SkillUploadPrecheckResult.STATUS_FORBIDDEN);
                    failed.addError(e.getErrMsg() != null ? e.getErrMsg() : e.getMessage());
                    results.add(failed);
                }
            }
            return results;
        }
}
