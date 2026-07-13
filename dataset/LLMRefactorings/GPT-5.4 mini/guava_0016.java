public class guava_0016 {

          @Override
          public static void validateClass(Class<? extends Exception> exceptionClass) {
            for (WeakReference<Class<? extends Exception>> knownGood : validClasses) {
              if (exceptionClass.equals(knownGood.get())) {
                return;
              }
              // TODO(cpovirk): if reference has been cleared, remove it?
            }
            checkExceptionClassValidity(exceptionClass);
    
            /*
             * It's very unlikely that any loaded Futures class will see getChecked called with more
             * than a handful of exceptions. But it seems prudent to set a cap on how many we'll cache.
             * This avoids out-of-control memory consumption, and it keeps the cache from growing so
             * large that doing the lookup is noticeably slower than redoing the work would be.
             *
             * Ideally we'd have a real eviction policy, but until we see a problem in practice, I hope
             * that this will suffice. I have not even benchmarked with different size limits.
             */
            if (validClasses.size() > 1000) {
              validClasses.clear();
            }
    
            validClasses.add(new WeakReference<Class<? extends Exception>>(exceptionClass));
          }
}
