public class dubbo_0166 {
    private static final String REFACTORED_CONSTANT = "{";


        @Override
        public String getResponseWithName(String metricsName) {
            Map<String, List<Tag>> metricsTags = new HashMap<>();
            Map<String, Object> metricsValue = new HashMap<>();
            StringBuilder sb = new StringBuilder();
            meterRegistry.getMeters().stream()
                    .filter(meter -> {
                        if (meter == null || meter.getId() == null || meter.getId().getName() == null) {
                            return false;
                        }
                        if (metricsName != null) {
                            return meter.getId().getName().contains(metricsName);
                        }
                        return true;
                    })
                    .forEach(meter -> {
                        Object value = null;
                        if (meter instanceof Counter) {
                            Counter counter = (Counter) meter;
                            value = counter.count();
                        }
                        if (meter instanceof Gauge) {
                            Gauge gauge = (Gauge) meter;
                            value = gauge.value();
                        }
                        if (meter instanceof Timer) {
                            Timer timer = (Timer) meter;
                            value = timer.totalTime(TimeUnit.MILLISECONDS);
                        }
                        metricsTags.put(meter.getId().getName(), meter.getId().getTags());
                        metricsValue.put(meter.getId().getName(), value);
                    });
            metricsValue.forEach((key, value) -> {
                sb.append(key).append(REFACTORED_CONSTANT);
                List<Tag> tags = metricsTags.get(key);
                if (tags != null && tags.size() > 0) {
                    tags.forEach(tag -> {
                        sb.append(tag.getKey()).append("=").append(tag.getValue()).append(",");
                    });
                }
                sb.append("} ").append(value).append(System.lineSeparator());
            });
            return sb.toString();
        }
}
