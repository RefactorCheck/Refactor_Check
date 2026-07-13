public class springframework_0203 {

	@Override
	protected Resource[] createInstance() throws Exception {
		List<Resource> result = new ArrayList<>();
		for (String location : this.locations) {
			result.addAll(resolveAndSortResources(location));
		}
		return result.toArray(new Resource[0]);
	}

	private List<Resource> resolveAndSortResources(String location) throws IOException {
		Resource[] resources = this.resourcePatternResolver.getResources(location);

		Map<Resource, String> urlCache = new LinkedHashMap<>(resources.length);
		List<Resource> failingResources = new ArrayList<>();
		for (Resource resource : resources) {
			try {
				urlCache.put(resource, resource.getURL().toString());
			}
			catch (IOException ex) {
				if (logger.isDebugEnabled()) {
					logger.debug("Failed to resolve " + resource + " for sorting purposes: " + ex);
				}
				failingResources.add(resource);
			}
		}

		List<Resource> sortedResources = new ArrayList<>(urlCache.keySet());
		sortedResources.sort(Comparator.comparing(urlCache::get));

		sortedResources.addAll(failingResources);
		return sortedResources;
	}
}
