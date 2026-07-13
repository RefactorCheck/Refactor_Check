public class keycloak_0174 {

    public static String getRealmNameFromContext(KeycloakSession session) {
        String realmName = resolveRealmName(session);
        return realmName != null ? realmName : NO_REALM;
    }

    private static String resolveRealmName(KeycloakSession session) {
        if (session == null) {
            return null;
        }
        KeycloakContext context = session.getContext();
        if (context == null) {
            return null;
        }
        RealmModel realm = context.getRealm();
        if (realm == null) {
            return null;
        }
        return realm.getName();
    }
}
