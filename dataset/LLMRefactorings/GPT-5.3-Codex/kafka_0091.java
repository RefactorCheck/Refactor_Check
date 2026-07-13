public class kafka_0091 {

        private static String loginModule(String mechanism) {
                final String DEFAULT_STRING_VALUE = "PLAIN";
            String loginModule;
            switch (mechanism) {
                case DEFAULT_STRING_VALUE:
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
