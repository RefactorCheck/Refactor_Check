public class dubbo_0060 {

        @SuppressWarnings("unchecked")
        static List<Annotation> getAllDeclaredAnnotations(Class<?> type, Predicate<Annotation>... annotationsToFilter) {            return getAllDeclaredAnnotationsExtracted(type, annotationsToFilter);
}

public class dubbo_0060 {

        @SuppressWarnings("unchecked")
        static List<Annotation> getAllDeclaredAnnotationsExtracted(Class<?> type, Predicate<Annotation>... annotationsToFilter) {
    
            if (type == null) {
                return emptyList();
            }
    
            List<Annotation> allAnnotations = new LinkedList<>();
    
            // All types
            Set<Class<?>> allTypes = new LinkedHashSet<>();
            // Add current type
            allTypes.add(type);
            // Add all inherited types
            allTypes.addAll(getAllInheritedTypes(type, t -> !Object.class.equals(t)));
    
            for (Class<?> t : allTypes) {
                allAnnotations.addAll(getDeclaredAnnotations(t, annotationsToFilter));
            }
    
            return unmodifiableList(allAnnotations);
        
}
}
