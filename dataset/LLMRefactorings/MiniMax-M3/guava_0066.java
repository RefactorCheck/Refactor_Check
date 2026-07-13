public class guava_0066 {

      public static Iterable<Annotation> getTesterAnnotations(AnnotatedElement classOrMethod) {
        synchronized (annotationCache) {
          List<Annotation> annotations = annotationCache.get(classOrMethod);
          if (annotations == null) {
            annotations = collectTesterAnnotations(classOrMethod);
            annotationCache.put(classOrMethod, annotations);
          }
          return annotations;
        }
      }

      private static List<Annotation> collectTesterAnnotations(AnnotatedElement classOrMethod) {
        List<Annotation> result = new ArrayList<>();
        for (Annotation a : classOrMethod.getDeclaredAnnotations()) {
          if (!(a instanceof NullMarked)
              && a.annotationType().isAnnotationPresent(TesterAnnotation.class)) {
            result.add(a);
          }
        }
        return unmodifiableList(result);
      }
}
