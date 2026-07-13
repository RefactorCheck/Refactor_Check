public class springframework_0111 {

    public static void copyRecursively(Path src, Path dest) throws IOException {
        Assert.notNull(src, "Source Path must not be null");
        Assert.notNull(dest, "Destination Path must not be null");
        BasicFileAttributes srcAttr = Files.readAttributes(src, BasicFileAttributes.class);

        if (srcAttr.isDirectory()) {
            if (src.getClass() == dest.getClass()) {
                copyDirectorySamePathType(src, dest);
            } else {
                copyDirectoryDifferentPathType(src, dest);
            }
        } else if (srcAttr.isRegularFile()) {
            Files.copy(src, dest);
        } else {
            throw new IllegalArgumentException("Source File must denote a directory or file");
        }
    }

    private static void copyDirectorySamePathType(Path src, Path dest) throws IOException {
        Files.walkFileTree(src, EnumSet.of(FOLLOW_LINKS), Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attr) throws IOException {
                Files.createDirectories(dest.resolve(src.relativize(dir)));
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws IOException {
                Files.copy(file, dest.resolve(src.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static void copyDirectoryDifferentPathType(Path src, Path dest) throws IOException {
        Files.walkFileTree(src, EnumSet.of(FOLLOW_LINKS), Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attr) throws IOException {
                Files.createDirectories(dest.resolve(src.relativize(dir).toString()));
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws IOException {
                Files.copy(file, dest.resolve(src.relativize(file).toString()), StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
