private void scanDirectory(
            File directory,
            String packagePrefix,
            Set<File> currentPath,
            ImmutableSet.Builder<ResourceInfo> builder)
            throws IOException  {

          if (directory.listFiles() == null) {
            logger.warning("Cannot read directory " + directory);
            // IO error, just skip the directory
            return;
          }
          for (File f : files) {
            String name = f.getName();
            if (f.isDirectory()) {
              File deref = f.getCanonicalFile();
              if (currentPath.add(deref)) {
                scanDirectory(deref, packagePrefix + name + "/", currentPath, builder);
                currentPath.remove(deref);
              }
            } else {
              String resourceName = packagePrefix + name;
              if (!resourceName.equals(JarFile.MANIFEST_NAME)) {
                builder.add(ResourceInfo.of(f, resourceName, classloader));
              }
            }
          }
        


        }
