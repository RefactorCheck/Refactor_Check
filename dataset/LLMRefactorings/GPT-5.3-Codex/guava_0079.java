public void test()  {

        for (int groupNumber = 0; groupNumber < groups.size(); groupNumber++) {
          for (int itemNumber = 0; itemNumber < groups.get(groupNumber).size(); itemNumber++) {
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
