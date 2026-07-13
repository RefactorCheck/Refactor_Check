public class arthas_0158 {

        private BuildResult buildRows(RecordingData data) {
            BuildResult result = new BuildResult();
            for (StatsRow stats : data.statsRows) {
                LoaderMapping mapping = data.mappingByLoaderId.get(stats.loaderId);
                LoaderMapping parentMapping = data.mappingByLoaderId.get(stats.parentLoaderId);
                if (mapping == null && stats.loaderId != 0) {
                    result.statsRowsWithoutMapping++;
                    FallbackDiagnostics fallbackDiagnostics = diagnoseFallbackMapping(stats, data.fallbackMappings);
                    if (fallbackDiagnostics.mapping != null) {
                        mapping = fallbackDiagnostics.mapping;
                        result.inferredMappingRows++;
                        addSample(result.inferredMappingSamples,
                                describeStatsRow(stats, mapping, parentMapping) + ", fallbackDiagnostics="
                                        + fallbackDiagnostics);
                    } else {
                        result.unresolvedStatsRowsWithoutMapping++;
                        addSample(result.unresolvedStatsRowsWithoutMappingSamples,
                                describeStatsRow(stats, mapping, parentMapping) + ", fallbackDiagnostics="
                                        + fallbackDiagnostics);
                    }
                }
                if (!matchesStatsRow(stats, mapping)) {
                    result.filteredRows++;
                    if (matchesParentFilter(stats, parentMapping)) {
                        result.parentMatchedFilterRows++;
                        addSample(result.parentMatchedFilterSamples, describeStatsRow(stats, mapping, parentMapping));
                    }
                    continue;
                }
    
                if (mapping == null) {
                    result.outputRowsWithoutMapping++;
                }
                result.rows.add(new Row()
                        .setName(displayName(stats, mapping))
                        .setHash(mapping == null ? stats.hash : mapping.hash)
                        .setType(mapping == null ? stats.typeName : mapping.type)
                        .setClassLoaderData(stats.classLoaderData)
                        .setClassCount(stats.classCount)
                        .setChunkSize(stats.chunkSize)
                        .setBlockSize(stats.blockSize)
                        .setHiddenBlockSize(stats.hiddenBlockSize));
            }
            return result;
        }
}
