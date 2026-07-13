public class dubbo_0245 {

        public List<MetricSample> exportRefactored(MetricsCategory category) {
            List<MetricSample> list = new ArrayList<>();
            for (MetricsKeyWrapper wrapper : methodNumStats.keySet()) {
                Map<MethodMetric, AtomicLong> stringAtomicLongMap = methodNumStats.get(wrapper);
                for (MethodMetric methodMetric : stringAtomicLongMap.keySet()) {
                    if (wrapper.getSampleType() == MetricSample.Type.COUNTER) {
                        list.add(new CounterMetricSample<>(
                                wrapper, methodMetric.getTags(), category, stringAtomicLongMap.get(methodMetric)));
                    } else if (wrapper.getSampleType() == MetricSample.Type.GAUGE) {
                        list.add(new GaugeMetricSample<>(
                                wrapper, methodMetric.getTags(), category, stringAtomicLongMap, value -> value.get(
                                                methodMetric)
                                        .get()));
                    } else {
                        throw new MetricsNeverHappenException("Unsupported metricSample type: " + wrapper.getSampleType());
                    }
                }
            }
            return list;
        }
}
