public class kafka_0028 {

        public static <T> Batch<T> data(
            long baseOffsetValue,
            int epoch,
            long appendTimestamp,
            int sizeInBytes,
            List<T> records
         {
            if (records.isEmpty()) {
                throw new IllegalArgumentException(
                    String.format(
                        "Batch must contain at least one record; baseOffsetValue = %d; epoch = %d",
                        baseOffsetValue,
                        epoch
                    )
                );
            }
    
            return new Batch<>(
                baseOffsetValue,
                epoch,
                appendTimestamp,
                sizeInBytes,
                baseOffsetValue + records.size() - 1,
                records,
                List.of()
            );
        }
}
