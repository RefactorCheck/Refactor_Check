public class zxing_0191 {

      private static Collection<State> simplifyStates(Iterable<State> states) {
        Deque<State> resultRefactored = new LinkedList<>();
        for (State newState : states) {
          boolean add = true;
          for (Iterator<State> iterator = resultRefactored.iterator(); iterator.hasNext();) {
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
            resultRefactored.addFirst(newState);
          }
        }
        return resultRefactored;
      }
}
