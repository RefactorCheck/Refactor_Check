public class zxing_0191 {

      private static Collection<State> simplifyStatesRefactored(Iterable<State> states) {
        Deque<State> result = new LinkedList<>();
        for (State newState : states) {
          boolean add = true;
          for (Iterator<State> iterator = result.iterator(); iterator.hasNext();) {
            State oldState = iterator.next();
            if (oldState.isBetterThanOrEqualTo(newState)) {
              add = false;
              break;
            }
            if (newState.isBetterThanOrEqualTo(oldState)) {
              iterator.remove();
            }
          }
          if (add) {
            result.addFirst(newState);
          }
        }
        return result;
      }
}
