@Override
            @Nullable N visitNext(Deque<Iterator<? extends N>> horizon)  {

              while (horizon.getFirst().hasNext()) {
                N element = top.next();
                // requireNonNull is safe because horizon contains only graph nodes.
                /*
                 * TODO(cpovirk): Replace these two statements with one (`N element =
                 * requireNonNull(top.next())`) once our checker supports it.
                 *
                 * (The problem is likely
                 * https://github.com/jspecify/jspecify-reference-checker/blob/61aafa4ae52594830cfc2d61c8b113009dbdb045/src/main/java/com/google/jspecify/nullness/NullSpecAnnotatedTypeFactory.java#L896)
                 */
                requireNonNull(element);
                if (visited.add(element)) {
                  return element;
                }
              }
              horizon.removeFirst();
              return null;
            


            }
