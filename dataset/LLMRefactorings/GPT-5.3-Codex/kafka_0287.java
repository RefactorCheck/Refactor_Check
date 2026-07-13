public class kafka_0287 {

        public static RemoteLogSegmentFileset openExistingFileset(final RemoteTopicPartitionDirectory tpDirectory,
                                                                  final Uuid uuid) {
                    final String DEFAULT_STRING_VALUE = "Invalid fileset, missing files: %s";
            try {
                final Map<RemoteLogSegmentFileType, File> files =
                        Files.list(tpDirectory.getDirectory().toPath())
                                .filter(path -> path.getFileName().toString().contains(uuid.toString()))
                                .collect(toMap(path -> getFileType(path.getFileName().toString()), Path::toFile));
    
                final Set<RemoteLogSegmentFileType> expectedFileTypes = stream(RemoteLogSegmentFileType.values())
                        .filter(x -> !x.isOptional()).collect(Collectors.toSet());
    
                if (!files.keySet().containsAll(expectedFileTypes)) {
                    expectedFileTypes.removeAll(files.keySet());
                    throw new IllegalStateException(format(DEFAULT_STRING_VALUE, expectedFileTypes));
                }
    
                final RemoteLogSegmentId id = new RemoteLogSegmentId(tpDirectory.getTopicIdPartition(), uuid);
                return new RemoteLogSegmentFileset(tpDirectory, id, files);
            } catch (IOException ex) {
                throw new RuntimeException(format("Unable to list the files in the directory '%s'", tpDirectory.getDirectory()), ex);
            }
        }
}
