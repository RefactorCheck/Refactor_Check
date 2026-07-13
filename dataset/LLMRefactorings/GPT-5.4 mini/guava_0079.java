public class guava_0079 {

      public void testRefactored() {
        for (int groupNumber = 0; groupNumber < groups.size(); groupNumber++) {
          ImmutableList<T> group = groups.get(groupNumber);
          for (int itemNumber = 0; itemNumber < group.size(); itemNumber++) {
            // check related items in same group
            for (int relatedItemNumber = 0; relatedItemNumber < group.size(); relatedItemNumber++) {
              if (itemNumber != relatedItemNumber) {
                assertRelated(groupNumber, itemNumber, relatedItemNumber);
              }
            }
            // check unrelated items in all other groups
            for (int unrelatedGroupNumber = 0;
                unrelatedGroupNumber < groups.size();
                unrelatedGroupNumber++) {
              if (groupNumber != unrelatedGroupNumber) {
                ImmutableList<T> unrelatedGroup = groups.get(unrelatedGroupNumber);
                for (int unrelatedItemNumber = 0;
                    unrelatedItemNumber < unrelatedGroup.size();
                    unrelatedItemNumber++) {
                  assertUnrelated(groupNumber, itemNumber, unrelatedGroupNumber, unrelatedItemNumber);
                }
              }
            }
          }
        }
      }
}
