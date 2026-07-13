public class arthas_0264 {

        private String normalizePath(String path) {
            if (path == null) {
                return null;
            }
    
            path = path.replaceAll("\\.\\./", "").replaceAll("\\./", "");
    
    
            path = path.startsWith("/") ? path : "/" + path;
    
    
            path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
    
    
            boolean hasAllowedExtension = ALLOWED_EXTENSIONS.stream()
                    .anyMatch(path::endsWith);
    
            if (!hasAllowedExtension) {
                return null;
            }
    
            return path;
        }
}
