public class kafka_0288 {

        private void normalizeAndValidate() {
            if (version() == 0) {
                normalizeVersionZeroPatternTypes();
            }

            validateFilters();
        }

        private void normalizeVersionZeroPatternTypes() {
            for (DeleteAclsRequestData.DeleteAclsFilter filter : data.filters()) {
                PatternType patternType = PatternType.fromCode(filter.patternTypeFilter());
                if (patternType == PatternType.ANY)
                    filter.setPatternTypeFilter(PatternType.LITERAL.code());
                else if (patternType != PatternType.LITERAL)
                    throw new UnsupportedVersionException("Version 0 does not support pattern type " +
                            patternType + " (only LITERAL and ANY are supported)");
            }
        }

        private void validateFilters() {
            final boolean unknown = data.filters().stream().anyMatch(filter ->
                    filter.patternTypeFilter() == PatternType.UNKNOWN.code()
                            || filter.resourceTypeFilter() == ResourceType.UNKNOWN.code()
                            || filter.operation() == AclOperation.UNKNOWN.code()
                            || filter.permissionType() == AclPermissionType.UNKNOWN.code()
            );

            if (unknown) {
                throw new IllegalArgumentException("Filters contain UNKNOWN elements, filters: " + data.filters());
            }
        }
}
