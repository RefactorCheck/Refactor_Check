public class kafka_0145 {

        private static final String FILE_DOES_NOT_EXIST_OR_EMPTY = "File does not exist or empty file provided.";

        static List<byte[]> readPayloadFile(String payloadFilePath, String payloadDelimiter) throws IOException {
            List<byte[]> payloadByteList = new ArrayList<>();
            if (payloadFilePath != null) {
                Path path = Paths.get(payloadFilePath);
                System.out.println("Reading payloads from: " + path.toAbsolutePath());
                if (Files.notExists(path) || Files.size(path) == 0)  {
                    throw new IllegalArgumentException(FILE_DOES_NOT_EXIST_OR_EMPTY);
                }
    
                try (Scanner payLoadScanner = new Scanner(path, StandardCharsets.UTF_8)) {
                    //setting the delimiter while parsing the file, avoids loading entire data in memory before split
                    payLoadScanner.useDelimiter(payloadDelimiter);
                    while (payLoadScanner.hasNext()) {
                        byte[] payloadBytes = payLoadScanner.next().getBytes(StandardCharsets.UTF_8);
                        payloadByteList.add(payloadBytes);
                    }
                }
    
                System.out.println("Number of records read: " + payloadByteList.size());
    
            }
            return payloadByteList;
        }
}
