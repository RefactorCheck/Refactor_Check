public class kafka_0091 {

        private static String loginModule(String mechanism) {
            switch (mechanism) {
                case "PLAIN":
                    return PlainLoginModule.class.getName();
                case "DIGEST-MD5":
                    return TestDigestLoginModule.class.getName();
                case "OAUTHBEARER":
                    return OAuthBearerLoginModule.class.getName();
                default:
                    if (ScramMechanism.isScram(mechanism))
                        return ScramLoginModule.class.getName();
                    else
                        throw new IllegalArgumentException("Unsupported mechanism " + mechanism);
            }
        }
}
