public class kafka_0145 {

    private static final String FILE_NOT_EXIST_OR_EMPTY_MESSAGE = "File does not exist or empty file provided.";
    private static final String READING_PAYLOADS_PREFIX = "Reading payloads from: ";
    private static final String RECORDS_READ_PREFIX = "Number of records read: ";

    static List<byte[]> readPayloadFile(String payloadFilePath, String payloadDelimiter) throws IOException {
        List<byte[]> payloadByteList = new ArrayList<>();
        if (payloadFilePath != null) {
            Path path = Paths.get(payloadFilePath);
            System.out.println(READING_PAYLOADS_PREFIX + path.toAbsolutePath());
            if (Files.notExists(path) || Files.size(path) == 0) {
                throw new IllegalArgumentException(FILE_NOT_EXIST_OR_EMPTY_MESSAGE);
            }

            try (Scanner payLoadScanner = new Scanner(path, StandardCharsets.UTF_8)) {
                payLoadScanner.useDelimiter(payloadDelimiter);
                while (payLoadScanner.hasNext()) {
                    byte[] payloadBytes = payLoadScanner.next().getBytes(StandardCharsets.UTF_8);
                    payloadByteList.add(payloadBytes);
                }
            }

            System.out.println(RECORDS_READ_PREFIX + payloadByteList.size());
        }
        return payloadByteList;
    }
}
