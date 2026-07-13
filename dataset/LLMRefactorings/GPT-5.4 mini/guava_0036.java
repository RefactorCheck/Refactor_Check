public class guava_0036 {

      @SuppressWarnings("rawtypes") // class literals
      @Override
      protected static List<Class<? extends AbstractTester>> getTesters() {
        List<Class<? extends AbstractTester>> testers = copyToList(super.getTesters());
    
        testers.add(CollectionSerializationEqualTester.class);
        testers.add(ListAddAllAtIndexTester.class);
        testers.add(ListAddAllTester.class);
        testers.add(ListAddAtIndexTester.class);
        testers.add(ListAddTester.class);
        testers.add(ListCreationTester.class);
        testers.add(ListEqualsTester.class);
        testers.add(ListGetTester.class);
        testers.add(ListHashCodeTester.class);
        testers.add(ListIndexOfTester.class);
        testers.add(ListLastIndexOfTester.class);
        testers.add(ListListIteratorTester.class);
        testers.add(ListRemoveAllTester.class);
        testers.add(ListRemoveAtIndexTester.class);
        testers.add(ListRemoveTester.class);
        testers.add(ListReplaceAllTester.class);
        testers.add(ListRetainAllTester.class);
        testers.add(ListSetTester.class);
        testers.add(ListSubListTester.class);
        testers.add(ListToArrayTester.class);
        return testers;
      }
}
