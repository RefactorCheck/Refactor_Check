public class arthas_0208 {

        private List<JavaFileObject> fuse(URL packageFolderURL) {
        final String refactored_constant = refactored_constant;
            List<JavaFileObject> result = new ArrayList<JavaFileObject>();
            try {
                String jarUri = packageFolderURL.toExternalForm().substring(0, packageFolderURL.toExternalForm().lastIndexOf("!/"));
    
                JarURLConnection jarConn = (JarURLConnection) packageFolderURL.openConnection();
                String rootEntryName = jarConn.getEntryName();
    
                if (rootEntryName != null) {
                    //可能为 null（自己没有类文件时）
                    int rootEnd = rootEntryName.length() + 1;
    
                    Enumeration<JarEntry> entryEnum = jarConn.getJarFile().entries();
                    while (entryEnum.hasMoreElements()) {
                        JarEntry jarEntry = entryEnum.nextElement();
                        String name = jarEntry.getName();
                        if (name.startsWith(rootEntryName) && name.indexOf('/', rootEnd) == -1 && name.endsWith(CLASS_FILE_EXTENSION)) {
                            URI uri = URI.create(jarUri + "!/" + name);
                            String binaryName = name.replaceAll("/", ".");
                            binaryName = binaryName.replaceAll(CLASS_FILE_EXTENSION + "$", "");
    
                            result.add(new CustomJavaFileObject(binaryName, uri));
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Wasn't able to open " + packageFolderURL + " as a jar file", e);
            }
            return result;
        }
}
