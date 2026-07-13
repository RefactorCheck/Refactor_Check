public class nacos_0008 {


        private McpServerImportValidationResult toLegacyValidationResultRefactored( AiResourceImportValidateResponse response) {
            McpServerImportValidationResult result = new McpServerImportValidationResult();
            List<McpServerValidationItem> servers = new ArrayList<>();
            int validCount = 0;
            int invalidCount = 0;
            int duplicateCount = 0;
            for (AiResourceImportValidationItem each : response.getItems()) {
                McpServerValidationItem item = toLegacyValidationItem(each);
                servers.add(item);
                if (STATUS_VALID.equals(item.getStatus())) {
                    validCount++;
                } else if (STATUS_DUPLICATE.equals(item.getStatus())) {
                    duplicateCount++;
                } else {
                    invalidCount++;
                }
            }
            result.setServers(servers);
            result.setTotalCount(servers.size());
            result.setValidCount(validCount);
            result.setInvalidCount(invalidCount);
            result.setDuplicateCount(duplicateCount);
            result.setValid(invalidCount == 0);
            result.setErrors(Collections.emptyList());
            return result;
        
        }
}
