public class guava_0296 {

            @Override
            @Nullable N visitNext(Deque<Iterator<? extends N>> horizon) {
              Iterator<? extends N> top = horizon.getFirst();
              N element = findNextUnvisited(top);
              if (element != null) {
                return element;
              }
              horizon.removeFirst();
              return null;
            }

            @Nullable
            private N findNextUnvisited(Iterator<? extends N> iterator) {
              while (iterator.hasNext()) {
                N element = iterator.next();
                // requireNonNull is safe because horizon contains only graph nodes.
                /*
                 * TODO(cpovirk): Replace these two statements with one (`N element =
                 * requireNonNull(iterator.next())`) once our checker supports it.
                 *
                 * (The problem is likely
                 * https://github.com/jspecify/jspecify-reference-checker/blob/61aafa4ae52594830cfc2d61c8b113009dbdb045/src/main/java/com/google/jspecify/nullness/NullSpecAnnotatedTypeFactory.java#L896)
                 */
                requireNonNull(element);
                if (visited.add(element)) {
                  return element;
                }
              }
              return null;
            }
}
