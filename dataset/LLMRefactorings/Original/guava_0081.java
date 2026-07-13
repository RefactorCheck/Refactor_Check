public class guava_0081 {

      static <T extends @Nullable Object, K extends Enum<K>, V>
          Collector<T, ?, ImmutableMap<K, V>> toImmutableEnumMap(
              Function<? super T, ? extends K> keyFunction,
              Function<? super T, ? extends V> valueFunction,
              BinaryOperator<V> mergeFunction) {
        checkNotNull(keyFunction);
        checkNotNull(valueFunction);
        checkNotNull(mergeFunction);
        // not UNORDERED because we don't know if mergeFunction is commutative
        return Collector.of(
            () -> new EnumMapAccumulator<K, V>(mergeFunction),
            (accum, t) -> {
              /*
               * We assign these to variables before calling checkNotNull to work around a bug in our
               * nullness checker.
               */
              K key = keyFunction.apply(t);
              V newValue = valueFunction.apply(t);
              accum.put(
                  checkNotNull(key, "Null key for input %s", t),
                  checkNotNull(newValue, "Null value for input %s", t));
            },
            EnumMapAccumulator::combine,
            EnumMapAccumulator::toImmutableMap);
      }
}
