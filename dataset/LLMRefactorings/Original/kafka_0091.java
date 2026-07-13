public class kafka_0091 {

        private static String loginModule(String mechanism) {
            String loginModule;
            switch (mechanism) {
                case "PLAIN":
                    loginModule = PlainLoginModule.class.getName();
                    break;
                case "DIGEST-MD5":
                    loginModule = TestDigestLoginModule.class.getName();
                    break;
                case "OAUTHBEARER":
                    loginModule = OAuthBearerLoginModule.class.getName();
                    break;
                default:
                    if (ScramMechanism.isScram(mechanism))
                        loginModule = ScramLoginModule.class.getName();
                    else
                        throw new IllegalArgumentException("Unsupported mechanism " + mechanism);
            }
            return loginModule;
        }
}
