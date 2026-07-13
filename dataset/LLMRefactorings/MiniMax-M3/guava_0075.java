import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;

public class guava_0075 {

      public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(Iterable<E> elements) {
        if (elements instanceof ImmutableEnumSet) {
          return (ImmutableEnumSet<E>) elements;
        } else if (elements instanceof Collection) {
          Collection<E> collection = (Collection<E>) elements;
          if (collection.isEmpty()) {
            return ImmutableSet.of();
          } else {
            return ImmutableEnumSet.asImmutable(EnumSet.copyOf(collection));
          }
        } else {
          return createEnumSetFromIterator(elements.iterator());
        }
      }

      private static <E extends Enum<E>> ImmutableSet<E> createEnumSetFromIterator(Iterator<E> itr) {
        if (itr.hasNext()) {
          EnumSet<E> enumSet = EnumSet.of(itr.next());
          addAll(enumSet, itr);
          return ImmutableEnumSet.asImmutable(enumSet);
        } else {
          return ImmutableSet.of();
        }
      }
}
