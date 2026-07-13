public class arthas_0127 {

        private RecordingData readRecording(Path recording, final boolean useCache) throws IOException {
            boolean cacheEnabled = useCache;
            Map<Long, LoaderMapping> mappingByLoaderId = new HashMap<Long, LoaderMapping>();
            List<LoaderMapping> fallbackMappings = new ArrayList<LoaderMapping>();
            List<String> mappingEventsWithoutLoaderSamples = new ArrayList<String>();
            Map<Long, StatsRow> latestStatsByLoaderId = new HashMap<Long, StatsRow>();
            long mappingEventCount = 0;
            long mappingEventWithoutLoaderCount = 0;
            long statsEventCount = 0;
            long duplicateStatsRowCount = 0;
            RecordingFile file = new RecordingFile(recording);
            try {
                while (file.hasMoreEvents()) {
                    RecordedEvent event = file.readEvent();
                    String eventName = event.getEventType().getName();
                    if (MAPPING_EVENT_NAME.equals(eventName)) {
                        mappingEventCount++;
                        RecordedClass anchorClass = event.getClass("anchorClass");
                        RecordedClassLoader loader = anchorClass == null ? null : anchorClass.getClassLoader();
                        LoaderMapping loaderMapping = LoaderMapping.from(event);
                        fallbackMappings.add(loaderMapping);
                        if (loader != null) {
                            mappingByLoaderId.put(loader.getId(), loaderMapping);
                        } else {
                            mappingEventWithoutLoaderCount++;
                            addSample(mappingEventsWithoutLoaderSamples, describeMapping(loaderMapping));
                        }
                    } else if (STATS_EVENT_NAME.equals(eventName)) {
                        statsEventCount++;
                        StatsRow row = StatsRow.from(event);
                        StatsRow previous = latestStatsByLoaderId.get(row.loaderId);
                        if (previous != null) {
                            duplicateStatsRowCount++;
                        }
                        if (previous == null || row.startTime.isAfter(previous.startTime)) {
                            latestStatsByLoaderId.put(row.loaderId, row);
                        }
                    }
                }
            } finally {
                file.close();
            }
            return new RecordingData(mappingByLoaderId, fallbackMappings, mappingEventsWithoutLoaderSamples,
                    latestStatsByLoaderId.values(), mappingEventCount, mappingEventWithoutLoaderCount, statsEventCount,
                    duplicateStatsRowCount);
        }
}
