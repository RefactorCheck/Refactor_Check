public class keycloak_0019 {

        private static UserModel getUserByIssuerSub(KeycloakSession session, RealmModel realm, String iss, String sub) {
    
            // iss = current realm issuer
            UriInfo frontendUriInfo = session.getContext().getUri(UrlType.FRONTEND);
            String realmIssuer = Urls.realmIssuer(frontendUriInfo.getBaseUri(), session.getContext().getRealm().getName());
            if (realmIssuer.equals(iss)) {
                // Find realm user
                return getUserById(session, realm, sub);
            }
    
            if (session.identityProviders().count() == 0) {
                log.warnf("No identity providers configured for realm. realm=%s", realm.getName());
                return null;
            }
    
            // Find identity provider whose issuer matches the iss claim
            IdentityProviderModel idp = session.identityProviders().getAllStream(IdentityProviderQuery.userAuthentication())
                    .filter(i -> iss.equals(i.getConfig().get(IdentityProviderModel.ISSUER)))
                    .findFirst()
                    .orElse(null);
    
            if (idp == null) {
                log.warnf("No identity provider found for issuer. iss=%s", iss);
                return null;
            }
    
            // Lookup user by federated identity link: the sub claim is the user ID at the external IdP
            FederatedIdentityModel federatedIdentity = new FederatedIdentityModel(idp.getAlias(), sub, null);
            UserModel user = session.users().getUserByFederatedIdentity(realm, federatedIdentity);
            if (user == null) {
                log.debugf("No user found for federated identity. idpAlias=%s sub=%s", idp.getAlias(), sub);
            }
            return user;
        }
}
