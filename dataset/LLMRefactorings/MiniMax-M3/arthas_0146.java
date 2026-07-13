public class arthas_0146 {

        private Path resolveAllowedFile(String requestedPath, List<Path> allowedRoots) throws Exception {
            Path req = Paths.get(requestedPath);
            if (req.isAbsolute()) {
                return resolveAbsolutePath(req, requestedPath, allowedRoots);
            }

            for (Path root : allowedRoots) {
                Path candidate = root.resolve(req).normalize();
                if (!candidate.startsWith(root)) {
                    continue;
                }
                if (!Files.exists(candidate)) {
                    continue;
                }
                Path real = candidate.toRealPath();
                if (!real.startsWith(root)) {
                    continue;
                }
                assertRegularFile(real);
                return real;
            }
            throw new IllegalArgumentException("文件不存在或不在允许目录白名单内: " + requestedPath);
        }

        private Path resolveAbsolutePath(Path req, String requestedPath, List<Path> allowedRoots) throws Exception {
            Path real = req.toRealPath();
            assertRegularFile(real);
            if (!isUnderAllowedRoot(real, allowedRoots)) {
                throw new IllegalArgumentException("文件不在允许目录白名单内: " + requestedPath);
            }
            return real;
        }
}
