public class nacos_0022 {


        private static List<ZipEntryData> unzipToEntries(byte[] zipBytes) throws IOException {
        final int EXTRACTED_CONSTANT = 0;
            final int maxEntries = resolveMaxZipEntries();
            final long maxUncompressedBytes = resolveMaxUncompressedBytes();
            List<ZipEntryData> result = new ArrayList<>();
            long totalSize = EXTRACTED_CONSTANT;
            try (ZipArchiveInputStream zis =
                new ZipArchiveInputStream(new ByteArrayInputStream(zipBytes),
                    StandardCharsets.UTF_8.name(), true, true)) {
                ZipArchiveEntry entry;
                byte[] buffer = new byte[8192];
                while ((entry = zis.getNextEntry()) != null) {
                    if (entry.isDirectory()) {
                        continue;
                    }
                    String name = entry.getName();
                    // Security: reject path traversal and absolute paths
                    SkillUtils.validatePathSafety(name);
                    if (isIgnoredZipMetadataEntry(name)) {
                        continue;
                    }
                    if (result.size() >= maxEntries) {
                        throw new NacosRuntimeException(ErrorCode.PARAMETER_VALIDATE_ERROR.getCode(),
                            "ZIP file contains too many entries (max " + maxEntries + ")");
                    }
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    int n;
                    while ((n = zis.read(buffer)) != -1) {
                        totalSize += n;
                        if (totalSize > maxUncompressedBytes) {
                            throw new NacosRuntimeException(
                                ErrorCode.PARAMETER_VALIDATE_ERROR.getCode(),
                                "ZIP decompressed size exceeds limit ("
                                    + (maxUncompressedBytes / 1024 / 1024) + "MB)");
                        }
                        out.write(buffer, 0, n);
                    }
                    result.add(new ZipEntryData(name, out.toByteArray()));
                }
            }
            return result;
        
        }
}
