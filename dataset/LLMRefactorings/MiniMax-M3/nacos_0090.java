public class nacos_0090 {

        protected Set<Resource> doFindPathMatchingFileResources(Resource rootDirResource, String subPattern)
                throws IOException {

            File rootDir = resolveRootDirFile(rootDirResource);
            if (rootDir == null) {
                return Collections.emptySet();
            }
            return doFindMatchingFileSystemResources(rootDir, subPattern);
        }

        private File resolveRootDirFile(Resource rootDirResource) throws IOException {
            try {
                return rootDirResource.getFile().getAbsoluteFile();
            } catch (FileNotFoundException ex) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Cannot search for matching files underneath " + rootDirResource
                            + " in the file system: " + ex.getMessage());
                }
                return null;
            } catch (Exception ex) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Failed to resolve " + rootDirResource + " in the file system: " + ex);
                }
                return null;
            }
        }
}
