public class springframework_0167 {

    	protected void testEvict(CacheableService<?> service, boolean successExpected) {
    		Cache cache = this.cm.getCache("testCache");
    
    		Object o1 = new Object();
    		cache.putIfAbsent(o1, -1L);
    		Object r1 = service.cache(o1);
    
    		service.evict(o1, null);
    		if (successExpected) {
    			assertThat(cache.get(o1)).isNull();
    		}
    		else {
    			assertThat(cache.get(o1)).isNotNull();
    		}
    
    		Object r2 = service.cache(o1);
    		if (successExpected) {
    			assertThat(r2).isNotSameAs(r1);
    		}
    		else {
    			assertThat(r2).isSameAs(r1);
    		}
    	}
}
