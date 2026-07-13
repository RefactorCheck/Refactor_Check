public class guava_0133 {

      @MapFeature.Require({SUPPORTS_PUT, ALLOWS_NULL_KEYS})
      @CollectionSize.Require(absent = ZERO)
      public void testComputeIfPresent_nullKeySupportedPresent() {
        initMapWithNullKey();
        K nullKey = null;
        V newValue = v3();
        assertEquals(
            "computeIfPresent(null, function) should return new value",
            newValue,
            getMap()
                .computeIfPresent(
                    nullKey,
                    (k, v) -> {
                      assertNull(k);
                      assertEquals(getValueForNullKey(), v);
                      return newValue;
                    }));
    
        Entry<K, V>[] expected = createArrayWithNullKey();
        expected[getNullLocation()] = entry(nullKey, newValue);
        expectContents(expected);
      }
}
