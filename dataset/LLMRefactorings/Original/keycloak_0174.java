public class keycloak_0174 {

        public static String getRealmNameFromContext(KeycloakSession session) {
            if(session == null) {
                return NO_REALM;
            }
    
            KeycloakContext context = session.getContext();
            if(context == null) {
                return NO_REALM;
            }
    
            RealmModel realm = context.getRealm();
            if (realm == null) {
                return NO_REALM;
            }
    
            if(realm.getName() != null) {
                return realm.getName();
            } else {
                return NO_REALM;
            }
        }
}
