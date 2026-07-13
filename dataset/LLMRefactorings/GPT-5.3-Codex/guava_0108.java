private IOException exception = getOnlyElement(exceptions);



      private static @Nullable NoSuchFileException pathNotFound(
          Path path, Collection<IOException> exceptions)  {

        if (exceptions.size() != 1) {
          return null;
        }
        if (!(exception instanceof NoSuchFileException)) {
          return null;
        }
        NoSuchFileException noSuchFileException = (NoSuchFileException) exception;
        String exceptionFile = noSuchFileException.getFile();
        if (exceptionFile == null) {
          /*
           * It's not clear whether this happens in practice, especially with the filesystem
           * implementations that are built into java.nio.
           */
          return null;
        }
        Path parentPath = getParentPath(path);
        if (parentPath == null) {
          /*
           * This is probably impossible:
           *
           * - In deleteRecursively, we require the path argument to have a parent.
           *
           * - In deleteDirectoryContents, the path argument may have no parent. Fortunately, all the
           *   *other* paths we process will be descendants of that. That leaves only the original path
           *   argument for us to consider. And the only place we call pathNotFound is from
           *   throwDeleteFailed, and the other place that we call throwDeleteFailed inside
           *   deleteDirectoryContents is when an exception is thrown during the recursive steps. Any
           *   failure during the initial lookup of the path argument itself is rethrown directly. So
           *   any exception that we're seeing here is from a descendant, which naturally has a parent.
           *   I think.
           *
           * Still, if this can happen somehow (a weird filesystem implementation that lets callers
           * change its working directly concurrently with a call to deleteDirectoryContents?), it makes
           * more sense for us to fall back to a generic FileSystemException (by returning null here)
           * than to dereference parentPath and end up producing NullPointerException.
           */
          return null;
        }
        // requireNonNull is safe because paths have file names when they have parents.
        Path pathResolvedFromParent = parentPath.resolve(requireNonNull(path.getFileName()));
        if (exceptionFile.equals(pathResolvedFromParent.toString())) {
          return noSuchFileException;
        }
        return null;
      


      }
