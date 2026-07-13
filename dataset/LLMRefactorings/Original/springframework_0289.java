public class springframework_0289 {

    	@Override
    	public final @Nullable FlashMap retrieveAndUpdate(HttpServletRequest request, HttpServletResponse response) {
    		List<FlashMap> allFlashMaps = retrieveFlashMaps(request);
    		if (CollectionUtils.isEmpty(allFlashMaps)) {
    			return null;
    		}
    
    		List<FlashMap> mapsToRemove = getExpiredFlashMaps(allFlashMaps);
    		FlashMap match = getMatchingFlashMap(allFlashMaps, request);
    		if (match != null) {
    			mapsToRemove.add(match);
    		}
    
    		if (!mapsToRemove.isEmpty()) {
    			Object mutex = getFlashMapsMutex(request);
    			if (mutex != null) {
    				synchronized (mutex) {
    					allFlashMaps = retrieveFlashMaps(request);
    					if (allFlashMaps != null) {
    						allFlashMaps.removeAll(mapsToRemove);
    						updateFlashMaps(allFlashMaps, request, response);
    					}
    				}
    			}
    			else {
    				allFlashMaps.removeAll(mapsToRemove);
    				updateFlashMaps(allFlashMaps, request, response);
    			}
    		}
    
    		return match;
    	}
}
