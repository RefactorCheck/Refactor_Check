public class guava_0133 {

      @MapFeature.Require({SUPPORTS_PUT, ALLOWS_NULL_KEYS})
      @CollectionSize.Require(absent = ZERO)
      public void testComputeIfPresent_nullKeySupportedPresentRefactored() {
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
}
