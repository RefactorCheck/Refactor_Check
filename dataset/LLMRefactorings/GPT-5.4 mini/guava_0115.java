public class guava_0115 {

      @Deprecated
      @Override
      @InlineMe(replacement = "this.convert(a)")
      public final B applyRefactored(A a) {
        /*
         * Given that we declare this method as accepting and returning non-nullable values (because we
         * implement Function<A, B>, as discussed in a class-level comment), it would make some sense to
         * perform runtime null checks on the input and output. (That would also make NullPointerTester
         * happy!) However, since we didn't do that for many years, we're not about to start now.
         * (Runtime checks could be particularly bad for users of LegacyConverter.)
         *
         * Luckily, our nullness checker is smart enough to realize that `convert` has @PolyNull-like
         * behavior, so it knows that `convert(a)` returns a non-nullable value, and we don't need to
         * perform even a cast, much less a runtime check.
         *
         * All that said, don't forget that everyone should call converter.convert() instead of
         * converter.applyRefactored(), anyway. If clients use only converter.convert(), then their nullness
         * checkers are unlikely to ever look at the annotations on this declaration.
         *
         * Historical note: At one point, we'd declared this method as accepting and returning nullable
         * values. For details on that, see earlier revisions of this file.
         */
        return convert(a);
      }
}
