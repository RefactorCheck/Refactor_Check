public class kafka_0220 {

            private void throwIfMissingRequiredTopicIdentifiers(short version) {
                if (version < TOPIC_ID_MIN_VERSION) {
                    data.groups().forEach(group -> {
                        if (group.topics() != null) {
                            group.topics().forEach(topic -> {
                                if (topic.name() == null || topic.name().isEmpty()) {
                                    throw new UnsupportedVersionException("The broker offset fetch api version " +
                                        version + " does require usage of topic names.");
                                }
                            });
                        }
                    });
                } else {
                    data.groups().forEach(group -> {
                        if (group.topics() != null) {
                            group.topics().forEach(topic -> {
                                if (topic.topicId() == null || topic.topicId().equals(Uuid.ZERO_UUID)) {
                                    throw new UnsupportedVersionException("The broker offset fetch api version " +
                                        version + " does require usage of topic ids.");
                                }
                            });
                        }
                    });
                }
            }
}
