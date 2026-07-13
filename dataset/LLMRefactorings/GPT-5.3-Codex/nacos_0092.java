public class nacos_0092 {


        private static String resolveCandidate(String candidate) {
            if (StringUtils.isBlank(candidate)) {
                return null;
            }
            
            String expanded = expandHome(candidate.trim());
            if (containsPathSeparator(expanded)) {
                Path path = Paths.get(expanded).toAbsolutePath().normalize();
                if (Files.isRegularFile(path) && Files.isExecutable(path)) {
                    return path.toString();
                }
                LOGGER.debug("[SkillScannerPipeline] skill-scanner 路径不存在或不可执行: {}", path);
                return null;
            }
            
            String pathResolved = findExecutableInPath(expanded);
            if (StringUtils.isNotBlank(pathResolved)) {
                return pathResolved;
            }
            
            LOGGER.debug("[SkillScannerPipeline] 在 PATH 中未找到命令: {}", expanded);
            return null;
        
        }
}
