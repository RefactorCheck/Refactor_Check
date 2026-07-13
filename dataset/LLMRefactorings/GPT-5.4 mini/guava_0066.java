public class guava_0066 {

      public static Iterable<Annotation> getTesterAnnotationsRefactored(AnnotatedElement classOrMethod) {
        synchronized (annotationCache) {
          List<Annotation> annotations = annotationCache.get(classOrMethod);
          if (annotations == null) {
            annotations = new ArrayList<>();
            for (Annotation a : classOrMethod.getDeclaredAnnotations()) {
              /*
               * We avoid reflecting on NullMarked because its @Target(..., MODULE) causes problems
               * under JDK 8.
               */
              if (!(a instanceof NullMarked)
                  && a.annotationType().isAnnotationPresent(TesterAnnotation.class)) {
                annotations.add(a);
              }
            }
            annotations = unmodifiableList(annotations);
            annotationCache.put(classOrMethod, annotations);
          }
          return annotations;
        }
      }
}
