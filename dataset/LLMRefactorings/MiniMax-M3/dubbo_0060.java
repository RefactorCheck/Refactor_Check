public class dubbo_0060 {

    @SuppressWarnings("unchecked")
    static List<Annotation> getAllDeclaredAnnotations(Class<?> type, Predicate<Annotation>... annotationsToFilter) {

        if (type == null) {
            return emptyList();
        }

        List<Annotation> allAnnotations = new LinkedList<>();
        Set<Class<?>> allTypes = getAllTypes(type);

        for (Class<?> t : allTypes) {
            allAnnotations.addAll(getDeclaredAnnotations(t, annotationsToFilter));
        }

        return unmodifiableList(allAnnotations);
    }

    private static Set<Class<?>> getAllTypes(Class<?> type) {
        Set<Class<?>> allTypes = new LinkedHashSet<>();
        allTypes.add(type);
        allTypes.addAll(getAllInheritedTypes(type, t -> !Object.class.equals(t)));
        return allTypes;
    }
}
