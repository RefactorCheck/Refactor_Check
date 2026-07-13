import java.util.Set;

public class guava_0139 {

        private static final int MULTIPLIER = 31;

        @Override
        public int hashCode() {
          // Warning: this is broken if size() == 0, so it is critical that we
          // substitute an empty ImmutableSet to the user in place of this
    
          // It's a weird formula, but tests prove it works.
          int adjust = size() - 1;
          for (int i = 0; i < axes.size(); i++) {
            adjust *= MULTIPLIER;
            adjust = ~~adjust;
            // in GWT, we have to deal with integer overflow carefully
          }
          int hash = 1;
          for (Set<E> axis : axes) {
            hash = MULTIPLIER * hash + (size() / axis.size() * axis.hashCode());
    
            hash = ~~hash;
          }
          hash += adjust;
          return ~~hash;
        }
}
