public class arthas_0208 {

        private List<JavaFileObject> fuse(URL packageFolderURL) {
            List<JavaFileObject> result = new ArrayList<JavaFileObject>();
            try {
                String jarUri = packageFolderURL.toExternalForm().substring(0, packageFolderURL.toExternalForm().lastIndexOf("!/"));
    
                JarURLConnection jarConn = (JarURLConnection) packageFolderURL.openConnection();
                String rootEntryName = jarConn.getEntryName();
    
                if (rootEntryName != null) {
                    int rootEnd = rootEntryName.length() + 1;
    
                    Enumeration<JarEntry> entryEnum = jarConn.getJarFile().entries();
                    while (entryEnum.hasMoreElements()) {
                        JarEntry jarEntry = entryEnum.nextElement();
                        String name = jarEntry.getName();
                        if (name.startsWith(rootEntryName) && name.indexOf('/', rootEnd) == -1 && name.endsWith(CLASS_FILE_EXTENSION)) {
                            result.add(createJavaFileObject(jarUri, name));
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Wasn't able to open " + packageFolderURL + " as a jar file", e);
            }
            return result;
        }
        
        private CustomJavaFileObject createJavaFileObject(String jarUri, String name) {
            URI uri = URI.create(jarUri + "!/" + name);
            String binaryName = name.replaceAll("/", ".");
            binaryName = binaryName.replaceAll(CLASS_FILE_EXTENSION + "$", "");
            return new CustomJavaFileObject(binaryName, uri);
        }
}
