public class keycloak_0177 {

        @Override
        public void validateConfiguration(KeycloakSession session, RealmModel realm, ComponentModel model) throws ComponentValidationException {
            super.validateConfiguration(session, realm, model);
    
            ConfigurationValidationHelper.check(model).checkList(getEcEllipticCurveProperty(), false);
    
            String ecInNistRep = model.get(getEcEllipticCurveKey());
            if (ecInNistRep == null) ecInNistRep = getDefaultEcEllipticCurve();
    
            if (!(model.contains(getEcPrivateKeyKey()) && model.contains(getEcPublicKeyKey()))) {
                generateKeys(model, ecInNistRep);
                getLogger().debugv("Generated keys for {0}", realm.getName());
            } else {
                regenerateKeysIfCurveChanged(model, ecInNistRep, realm);
            }
        }

        private void regenerateKeysIfCurveChanged(ComponentModel model, String ecInNistRep, RealmModel realm) {
            String currentEc = getCurveFromPublicKey(model.getConfig().getFirst(getEcPublicKeyKey()));
            if (!ecInNistRep.equals(currentEc)) {
                generateKeys(model, ecInNistRep);
                getLogger().debugv("Elliptic Curve changed, generating new keys for {0}", realm.getName());
            }
        }
}
