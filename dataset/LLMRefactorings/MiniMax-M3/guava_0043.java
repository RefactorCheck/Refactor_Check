public class guava_0043 {

      private static TimeUnit chooseUnit(long nanos) {
        if (greaterThanZero(nanos, DAYS)) {
          return DAYS;
        }
        if (greaterThanZero(nanos, HOURS)) {
          return HOURS;
        }
        if (greaterThanZero(nanos, MINUTES)) {
          return MINUTES;
        }
        if (greaterThanZero(nanos, SECONDS)) {
          return SECONDS;
        }
        if (greaterThanZero(nanos, MILLISECONDS)) {
          return MILLISECONDS;
        }
        if (greaterThanZero(nanos, MICROSECONDS)) {
          return MICROSECONDS;
        }
        return NANOSECONDS;
      }

      private static boolean greaterThanZero(long nanos, TimeUnit unit) {
        return unit.convert(nanos, NANOSECONDS) > 0;
      }
}
