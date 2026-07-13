public class kafka_0279 {

        @SuppressWarnings("checkstyle:cyclomaticComplexity")
        private static boolean isUpgrade(final Map<String, ?> configs) {
            final Object upgradeFrom = configs.get(StreamsConfig.UPGRADE_FROM_CONFIG);
            if (upgradeFrom == null) {
                return false;
            }
            return isSupportedUpgradeVersion((String) upgradeFrom);
        }

        private static boolean isSupportedUpgradeVersion(final String upgradeFrom) {
            switch (UpgradeFromValues.fromString(upgradeFrom)) {
                case UPGRADE_FROM_24:
                case UPGRADE_FROM_25:
                case UPGRADE_FROM_26:
                case UPGRADE_FROM_27:
                case UPGRADE_FROM_28:
                case UPGRADE_FROM_30:
                case UPGRADE_FROM_31:
                case UPGRADE_FROM_32:
                case UPGRADE_FROM_33:
                case UPGRADE_FROM_34:
                    return true;
                default:
                    return false;
            }
        }
}
