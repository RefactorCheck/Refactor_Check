public class springframework_0289 {

    	@Override
    	public final @Nullable FlashMap retrieveAndUpdate(HttpServletRequest request, HttpServletResponse response) {
    		if (CollectionUtils.isEmpty((retrieveFlashMaps(request)))) {
    			return null;
    		}
    
    		List<FlashMap> mapsToRemove = getExpiredFlashMaps((retrieveFlashMaps(request)));
    		FlashMap match = getMatchingFlashMap((retrieveFlashMaps(request)), request);
    		if (match != null) {
    			mapsToRemove.add(match);
    		}
    
    		if (!mapsToRemove.isEmpty()) {
    			Object mutex = getFlashMapsMutex(request);
    			if (mutex != null) {
    				synchronized (mutex) {
    					(retrieveFlashMaps(request)) = retrieveFlashMaps(request);
    					if ((retrieveFlashMaps(request)) != null) {
    						(retrieveFlashMaps(request)).removeAll(mapsToRemove);
    						updateFlashMaps((retrieveFlashMaps(request)), request, response);
    					}
    				}
    			}
    			else {
    				(retrieveFlashMaps(request)).removeAll(mapsToRemove);
    				updateFlashMaps((retrieveFlashMaps(request)), request, response);
    			}
    		}
    
    		return match;
    	}
}
