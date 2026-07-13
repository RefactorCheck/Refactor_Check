public class nacos_0107 {

    @Override
    public boolean onSnapshotLoad(SnapshotReader reader) {
        final Map<String, LocalFileMeta> metaMap =
            new HashMap<>(reader.listFiles().size());
        for (String fileName : reader.listFiles()) {
            final LocalFileMetaOutter.LocalFileMeta meta =
                (LocalFileMetaOutter.LocalFileMeta) reader
                    .getFileMeta(fileName);

            byte[] bytes = meta.getUserMeta().toByteArray();

            final LocalFileMeta fileMeta = createFileMeta(bytes);

            metaMap.put(fileName, fileMeta);
        }
        final Reader rCtx = new Reader(reader.getPath(), metaMap);
        return item.onSnapshotLoad(rCtx);
    }

    private LocalFileMeta createFileMeta(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return new LocalFileMeta();
        }
        return JacksonUtils.toObj(bytes, LocalFileMeta.class);
    }
}
