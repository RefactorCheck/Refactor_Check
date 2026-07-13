public class kafka_0026 {

        @Setup(Level.Trial)
        public void setup() {
            Random random = new Random();
    
            MetadataDelta delta = new MetadataDelta.Builder()
                .setImage(MetadataImage.EMPTY)
                .build();
            for (int i = 0; i < topicCount; i++) {
                String topicName =
                    WORDS.get(random.nextInt(WORDS.size())) + "_" +
                    WORDS.get(random.nextInt(WORDS.size())) + "_" +
                    i;
    
                delta.replay(new TopicRecord()
                    .setTopicId(Uuid.randomUuid())
                    .setName(topicName));
            }
            image = delta.apply(MetadataProvenance.EMPTY);
    
            regexes = new HashSet<>();
            for (int i = 0; i < regexCount; i++) {
                regexes.add(".*" + WORDS.get(random.nextInt(WORDS.size())) + ".*");
            }
        }
}
