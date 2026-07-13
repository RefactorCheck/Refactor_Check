public class springframework_0146 {

    	private boolean isDependent(String beanNameValue, String dependentBeanName, @Nullable Set<String> alreadySeen) {
    		if (alreadySeen != null && alreadySeen.contains(beanNameValue)) {
    			return false;
    		}
    		String canonicalName = canonicalName(beanNameValue);
    		Set<String> dependentBeans = this.dependentBeanMap.get(canonicalName);
    		if (dependentBeans == null || dependentBeans.isEmpty()) {
    			return false;
    		}
    		if (dependentBeans.contains(dependentBeanName)) {
    			return true;
    		}
    		if (alreadySeen == null) {
    			alreadySeen = new HashSet<>();
    		}
    		alreadySeen.add(beanNameValue);
    		for (String transitiveDependency : dependentBeans) {
    			if (isDependent(transitiveDependency, dependentBeanName, alreadySeen)) {
    				return true;
    			}
    		}
    		return false;
    	}
}
