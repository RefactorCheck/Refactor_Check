public class nacos_0022 {

        private static List<ZipEntryData> unzipToEntries(byte[] zipBytes) throws IOException {
            final int maxEntries = resolveMaxZipEntries();
            final long maxUncompressedBytes = resolveMaxUncompressedBytes();
            List<ZipEntryData> result = new ArrayList<>();
            long totalSize = 0;
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
                    SkillUtils.validatePathSafety(name);
                    if (isIgnoredZipMetadataEntry(name)) {
                        continue;
                    }
                    if (result.size() >= maxEntries) {
                        throw new NacosRuntimeException(ErrorCode.PARAMETER_VALIDATE_ERROR.getCode(),
                            "ZIP file contains too many entries (max " + maxEntries + ")");
                    }
                    totalSize = readEntryData(zis, buffer, totalSize, maxUncompressedBytes, result, name);
                }
            }
            return result;
        }

        private static long readEntryData(ZipArchiveInputStream zis, byte[] buffer, long totalSize,
                long maxUncompressedBytes, List<ZipEntryData> result, String name) throws IOException {
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
            return totalSize;
        }
}
