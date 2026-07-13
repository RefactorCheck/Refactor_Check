public class keycloak_0053 {

        public ConfigurableRequiredActionProviderRepresentation toRepresentation(RequiredActionProviderModel model) {
            ConfigurableRequiredActionProviderRepresentation rep = new ConfigurableRequiredActionProviderRepresentation();
            populateRepresentation(rep, model);

            RequiredActionFactory factory = (RequiredActionFactory)session.getKeycloakSessionFactory().getProviderFactory(RequiredActionProvider.class, model.getProviderId());
            if (factory != null) {
                rep.setConfigurable(factory.isConfigurable());
            } else {
                logger.warnv("Detected RequiredAction with missing provider. realm={0}, alias={1}, providerId={2}",
                        realm.getName(), model.getAlias(), model.getProviderId());
            }

            return rep;
        }

        private void populateRepresentation(ConfigurableRequiredActionProviderRepresentation rep, RequiredActionProviderModel model) {
            rep.setAlias(model.getAlias());
            rep.setProviderId(model.getProviderId());
            rep.setName(model.getName());
            rep.setDefaultAction(model.isDefaultAction());
            rep.setPriority(model.getPriority());
            rep.setEnabled(model.isEnabled());
            rep.setConfig(model.getConfig());
        }
}
