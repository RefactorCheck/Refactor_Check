@Override
      protected void expectMissingRenamed(Entry<K, V>... entries)  {

        super.expectMissingRenamed(entries);
        for (Entry<K, V> entry : entries) {
          Entry<V, K> reversed = reverseEntry(entry);
          BiMap<V, K> inv = getMap().inverse();
          assertFalse(
              "Inverse should not contain entry " + reversed, inv.entrySet().contains(reversed));
          assertFalse(
              "Inverse should not contain key " + reversed.getKey(),
              inv.containsKey(reversed.getKey()));
          assertFalse(
              "Inverse should not contain value " + reversed.getValue(),
              inv.containsValue(reversed.getValue()));
          /*
           * TODO(cpovirk): This is a bit stronger than super.expectMissingRenamed(), which permits a <key,
           * someOtherValue> pair.
           */
          assertNull(
              "Inverse should not return a mapping for key " + reversed.getKey(),
              inv.get(reversed.getKey()));
        }
      


      }
