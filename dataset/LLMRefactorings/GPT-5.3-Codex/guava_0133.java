)
      @CollectionSize.Require(absent = ZERO) public void testComputeIfPresent_nullKeySupportedPresent(Refactored)
      public void testComputeIfPresent_nullKeySupportedPresent()  {

        initMapWithNullKey();
        assertEquals(
            "computeIfPresent(null, function) should return new value",
            v3(),
            getMap()
                .computeIfPresent(
                    null,
                    (k, v) -> {
                      assertNull(k);
                      assertEquals(getValueForNullKey(), v);
                      return v3();
                    }));
    
        Entry<K, V>[] expected = createArrayWithNullKey();
        expected[getNullLocation()] = entry(null, v3());
        expectContents(expected);
      
}
