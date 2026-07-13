public class guava_0007 {

      private static <K extends @Nullable Object, V extends @Nullable Object> void doDifference(
          Map<? extends K, ? extends V> left,
          Map<? extends K, ? extends V> right,
          Equivalence<? super @NonNull V> valueEquivalence,
          Map<K, V> onlyOnLeft,
          Map<K, V> onlyOnRight,
          Map<K, V> onBoth,
          Map<K, ValueDifference<V>> differences) {
        for (Entry<? extends K, ? extends V> entry : left.entrySet()) {
          K leftKey = entry.getKey();
          V leftValue = entry.getValue();
          if (right.containsKey(leftKey)) {
            /*
             * The cast is safe because onlyOnRight contains all the keys of right.
             *
             * TODO(cpovirk): Consider checking onlyOnRight.containsKey instead of right.containsKey.
             * That could change behavior if the input maps use different equivalence relations (and so
             * a key that appears once in `right` might appear multiple times in `left`). We don't
             * guarantee behavior in that case, anyway, and the current behavior is likely undesirable.
             * So that's either a reason to feel free to change it or a reason to not bother thinking
             * further about this.
             */
            V rightValue = uncheckedCastNullableTToT(onlyOnRight.remove(leftKey));
            if (valueEquivalence.equivalent(leftValue, rightValue)) {
              onBoth.put(leftKey, leftValue);
            } else {
              differences.put(leftKey, ValueDifferenceImpl.create(leftValue, rightValue));
            }
          } else {
            onlyOnLeft.put(leftKey, leftValue);
          }
        }
      }
}
