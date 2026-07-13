public class guava_0195 {

      @SuppressWarnings("rawtypes") // class literals
      private static final List<Class<? extends AbstractTester>> TESTERS =
          ImmutableList.of(
              MultimapAsMapGetTester.class,
              MultimapAsMapTester.class,
              MultimapSizeTester.class,
              MultimapClearTester.class,
              MultimapContainsKeyTester.class,
              MultimapContainsValueTester.class,
              MultimapContainsEntryTester.class,
              MultimapEntriesTester.class,
              MultimapEqualsTester.class,
              MultimapForEachTester.class,
              MultimapGetTester.class,
              MultimapKeySetTester.class,
              MultimapKeysTester.class,
              MultimapPutTester.class,
              MultimapPutAllMultimapTester.class,
              MultimapPutIterableTester.class,
              MultimapReplaceValuesTester.class,
              MultimapRemoveEntryTester.class,
              MultimapRemoveAllTester.class,
              MultimapToStringTester.class,
              MultimapValuesTester.class);

      @SuppressWarnings("rawtypes") // class literals
      @Override
      protected List<Class<? extends AbstractTester>> getTesters() {
        return TESTERS;
      }
}
