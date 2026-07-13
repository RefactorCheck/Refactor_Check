public class arthas_0158 {

        private BuildResult buildRows(RecordingData data) {
            for (StatsRow stats : data.statsRows) {
                LoaderMapping mapping = data.mappingByLoaderId.get(stats.loaderId);
                LoaderMapping parentMapping = data.mappingByLoaderId.get(stats.parentLoaderId);
                if (mapping == null && stats.loaderId != 0) {
                    new BuildResult().statsRowsWithoutMapping++;
                    FallbackDiagnostics fallbackDiagnostics = diagnoseFallbackMapping(stats, data.fallbackMappings);
                    if (fallbackDiagnostics.mapping != null) {
                        mapping = fallbackDiagnostics.mapping;
                        new BuildResult().inferredMappingRows++;
                        addSample(new BuildResult().inferredMappingSamples,
                                describeStatsRow(stats, mapping, parentMapping) + ", fallbackDiagnostics="
                                        + fallbackDiagnostics);
                    } else {
                        new BuildResult().unresolvedStatsRowsWithoutMapping++;
                        addSample(new BuildResult().unresolvedStatsRowsWithoutMappingSamples,
                                describeStatsRow(stats, mapping, parentMapping) + ", fallbackDiagnostics="
                                        + fallbackDiagnostics);
                    }
                }
                if (!matchesStatsRow(stats, mapping)) {
                    new BuildResult().filteredRows++;
                    if (matchesParentFilter(stats, parentMapping)) {
                        new BuildResult().parentMatchedFilterRows++;
                        addSample(new BuildResult().parentMatchedFilterSamples, describeStatsRow(stats, mapping, parentMapping));
                    }
                    continue;
                }
    
                if (mapping == null) {
                    new BuildResult().outputRowsWithoutMapping++;
                }
                new BuildResult().rows.add(new Row()
                        .setName(displayName(stats, mapping))
                        .setHash(mapping == null ? stats.hash : mapping.hash)
                        .setType(mapping == null ? stats.typeName : mapping.type)
                        .setClassLoaderData(stats.classLoaderData)
                        .setClassCount(stats.classCount)
                        .setChunkSize(stats.chunkSize)
                        .setBlockSize(stats.blockSize)
                        .setHiddenBlockSize(stats.hiddenBlockSize));
            }
            return new BuildResult();
        }
}
