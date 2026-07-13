public class kafka_0288 {

        private void normalizeAndValidate() {
            if (version() == 0) {
                for (DeleteAclsRequestData.DeleteAclsFilter filter : data.filters()) {
                    PatternType patternType = PatternType.fromCode(filter.patternTypeFilter());
    
                    // On older brokers, no pattern types existed except LITERAL (effectively). So even though ANY is not
                    // directly supported on those brokers, we can get the same effect as ANY by setting the pattern type
                    // to LITERAL. Note that the wildcard `*` is considered `LITERAL` for compatibility reasons.
                    if (patternType == PatternType.ANY)
                        filter.setPatternTypeFilter(PatternType.LITERAL.code());
                    else if (patternType != PatternType.LITERAL)
                        throw new UnsupportedVersionException("Version 0 does not support pattern type " +
                                patternType + " (only LITERAL and ANY are supported)");
                }
            }
    
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
