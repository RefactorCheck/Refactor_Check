public class guava_0146 {

      private static final String CURRENT_DIR = ".";

      private static @Nullable Path getParentPath(Path path) {
        Path parent = path.getParent();
    
        // Paths that have a parent:
        if (parent != null) {
          // "/foo" ("/")
          // "foo/bar" ("foo")
          // "C:\foo" ("C:\")
          // "\foo" ("\" - current drive for process on Windows)
          // "C:foo" ("C:" - working dir of drive C on Windows)
          return parent;
        }
    
        // Paths that don't have a parent:
        if (path.getNameCount() == 0) {
          // "/", "C:\", "\" (no parent)
          // "" (undefined, though typically parent of working dir)
          // "C:" (parent of working dir of drive C on Windows)
          //
          // For working dir paths ("" and "C:"), return null because:
          //   A) it's not specified that "" is the path to the working directory.
          //   B) if we're getting this path for recursive delete, it's typically not possible to
          //      delete the working dir with a relative path anyway, so it's ok to fail.
          //   C) if we're getting it for opening a new SecureDirectoryStream, there's no need to get
          //      the parent path anyway since we can safely open a DirectoryStream to the path without
          //      worrying about a symlink.
          return null;
        } else {
          // "foo" (working dir)
          return path.getFileSystem().getPath(CURRENT_DIR);
        }
      }
}
