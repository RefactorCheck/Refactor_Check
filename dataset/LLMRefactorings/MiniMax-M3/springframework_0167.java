public class springframework_0167 {

    protected void testEvict(CacheableService<?> service, boolean successExpected) {
        Cache cache = this.cm.getCache("testCache");

        Object o1 = new Object();
        cache.putIfAbsent(o1, -1L);
        Object r1 = service.cache(o1);

        service.evict(o1, null);
        assertEvictionState(cache, o1, successExpected);

        Object r2 = service.cache(o1);
        assertCacheResult(r1, r2, successExpected);
    }

    private void assertEvictionState(Cache cache, Object key, boolean successExpected) {
        if (successExpected) {
            assertThat(cache.get(key)).isNull();
        }
        else {
            assertThat(cache.get(key)).isNotNull();
        }
    }

    private void assertCacheResult(Object r1, Object r2, boolean successExpected) {
        if (successExpected) {
            assertThat(r2).isNotSameAs(r1);
        }
        else {
            assertThat(r2).isSameAs(r1);
        }
    }
}
