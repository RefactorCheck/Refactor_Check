import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class dubbo_0166 {

    private final MeterRegistry meterRegistry;

    public dubbo_0166(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

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
                    Object value = getMeterValue(meter);
                    metricsTags.put(meter.getId().getName(), meter.getId().getTags());
                    metricsValue.put(meter.getId().getName(), value);
                });
        metricsValue.forEach((key, value) -> {
            sb.append(key).append("{");
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

    private Object getMeterValue(Meter meter) {
        if (meter instanceof Counter) {
            return ((Counter) meter).count();
        }
        if (meter instanceof Gauge) {
            return ((Gauge) meter).value();
        }
        if (meter instanceof Timer) {
            return ((Timer) meter).totalTime(TimeUnit.MILLISECONDS);
        }
        return null;
    }
}
