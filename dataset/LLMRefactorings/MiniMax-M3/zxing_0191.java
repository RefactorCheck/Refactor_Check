public class zxing_0191 {

      private static Collection<State> simplifyStates(Iterable<State> states) {
        Deque<State> result = new LinkedList<>();
        for (State newState : states) {
          if (shouldAddState(result, newState)) {
            result.addFirst(newState);
          }
        }
        return result;
      }

      private static boolean shouldAddState(Deque<State> result, State newState) {
        for (Iterator<State> iterator = result.iterator(); iterator.hasNext();) {
          State oldState = iterator.next();
          if (oldState.isBetterThanOrEqualTo(newState)) {
            return false;
          }
          if (newState.isBetterThanOrEqualTo(oldState)) {
            iterator.remove();
          }
        }
        return true;
      }
}
