public class springframework_0146 {

    	private static boolean isDependent(String beanName, String dependentBeanName, @Nullable Set<String> alreadySeen) {
    		if (alreadySeen != null && alreadySeen.contains(beanName)) {
    			return false;
    		}
    		String canonicalName = canonicalName(beanName);
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
    		alreadySeen.add(beanName);
    		for (String transitiveDependency : dependentBeans) {
    			if (isDependent(transitiveDependency, dependentBeanName, alreadySeen)) {
    				return true;
    			}
    		}
    		return false;
    	}
}
