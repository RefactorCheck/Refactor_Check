public static Iterable<Annotation> getTesterAnnotations(AnnotatedElement classOrMethod)  {

        synchronized (annotationCache) {
          if (annotationCache.get(classOrMethod) == null) {
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
