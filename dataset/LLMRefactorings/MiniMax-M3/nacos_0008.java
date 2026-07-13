public class nacos_0008 {

    private McpServerImportValidationResult toLegacyValidationResult(
            AiResourceImportValidateResponse response) {
        McpServerImportValidationResult result = new McpServerImportValidationResult();
        ValidationCounts counts = processValidationItems(response.getItems());
        result.setServers(counts.servers);
        result.setTotalCount(counts.servers.size());
        result.setValidCount(counts.validCount);
        result.setInvalidCount(counts.invalidCount);
        result.setDuplicateCount(counts.duplicateCount);
        result.setValid(counts.invalidCount == 0);
        result.setErrors(Collections.emptyList());
        return result;
    }

    private ValidationCounts processValidationItems(List<AiResourceImportValidationItem> items) {
        List<McpServerValidationItem> servers = new ArrayList<>();
        int validCount = 0;
        int invalidCount = 0;
        int duplicateCount = 0;
        for (AiResourceImportValidationItem each : items) {
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
        return new ValidationCounts(servers, validCount, invalidCount, duplicateCount);
    }

    private static class ValidationCounts {
        final List<McpServerValidationItem> servers;
        final int validCount;
        final int invalidCount;
        final int duplicateCount;

        ValidationCounts(List<McpServerValidationItem> servers, int validCount, int invalidCount, int duplicateCount) {
            this.servers = servers;
            this.validCount = validCount;
            this.invalidCount = invalidCount;
            this.duplicateCount = duplicateCount;
        }
    }
}
