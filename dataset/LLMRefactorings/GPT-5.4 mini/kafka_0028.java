public class kafka_0028 {

        public static <T> Batch<T> data(
            long baseOffset,
            int epoch,
            long appendTimestamp,
            int sizeInBytes,
            List<T> records
        ) {
            if (records.isEmpty()) {
                throw new IllegalArgumentException(
                    String.format(
                        "Batch must contain at least one record; baseOffset = %d; epoch = %d",
                        baseOffset,
                        epoch
                    )
                );
            }
            long lastOffset = baseOffset + records.size() - 1;
    
            return new Batch<>(
                baseOffset,
                epoch,
                appendTimestamp,
                sizeInBytes,
                lastOffset,
                records,
                List.of()
            );
        }
}
