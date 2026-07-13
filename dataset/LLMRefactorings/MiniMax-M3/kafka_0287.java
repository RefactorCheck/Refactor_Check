public class kafka_0287 {

    public static RemoteLogSegmentFileset openExistingFileset(final RemoteTopicPartitionDirectory tpDirectory,
                                                              final Uuid uuid) {
        try {
            final Map<RemoteLogSegmentFileType, File> files = collectFilesForUuid(tpDirectory, uuid);

            final Set<RemoteLogSegmentFileType> expectedFileTypes = stream(RemoteLogSegmentFileType.values())
                    .filter(x -> !x.isOptional()).collect(Collectors.toSet());

            if (!files.keySet().containsAll(expectedFileTypes)) {
                expectedFileTypes.removeAll(files.keySet());
                throw new IllegalStateException(format("Invalid fileset, missing files: %s", expectedFileTypes));
            }

            final RemoteLogSegmentId id = new RemoteLogSegmentId(tpDirectory.getTopicIdPartition(), uuid);
            return new RemoteLogSegmentFileset(tpDirectory, id, files);
        } catch (IOException ex) {
            throw new RuntimeException(format("Unable to list the files in the directory '%s'", tpDirectory.getDirectory()), ex);
        }
    }

    private static Map<RemoteLogSegmentFileType, File> collectFilesForUuid(final RemoteTopicPartitionDirectory tpDirectory,
                                                                            final Uuid uuid) throws IOException {
        return Files.list(tpDirectory.getDirectory().toPath())
                .filter(path -> path.getFileName().toString().contains(uuid.toString()))
                .collect(toMap(path -> getFileType(path.getFileName().toString()), Path::toFile));
    }
}
