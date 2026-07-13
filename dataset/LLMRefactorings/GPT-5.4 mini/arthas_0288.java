public class arthas_0288 {
    private JarURLConnection jarConn;


            private void loadIndex() throws IOException {
                jarConn = (JarURLConnection) uri.toURL().openConnection();
                String rootEntryName = jarConn.getEntryName() == null ? "" : jarConn.getEntryName();
                Enumeration<JarEntry> entryEnum = jarConn.getJarFile().entries();
                while (entryEnum.hasMoreElements()) {
                    JarEntry jarEntry = entryEnum.nextElement();
                    String entryName = jarEntry.getName();
                    if (entryName.startsWith(rootEntryName) && entryName.endsWith(CLASS_FILE_EXTENSION)) {
                        String className = entryName
                                .substring(0, entryName.length() - CLASS_FILE_EXTENSION.length())
                                .replace(rootEntryName, "")
                                .replace("/", ".");
                        if (className.startsWith(".")) className = className.substring(1);
                        if (className.equals("package-info")
                                || className.equals("module-info")
                                || className.lastIndexOf(".") == -1) {
                            continue;
                        }
                        String packageName = className.substring(0, className.lastIndexOf("."));
                        List<ClassUriWrapper> classes = packages.get(packageName);
                        if (classes == null) {
                            classes = new ArrayList<>();
                            packages.put(packageName, classes);
                        }
                        classes.add(new ClassUriWrapper(className, URI.create(jarUri + "!/" + entryName)));
                    }
                }
            }
}
