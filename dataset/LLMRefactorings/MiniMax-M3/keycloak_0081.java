public class keycloak_0081 {

    protected ConfigData ensureAuthInfo(ConfigData config) {
        if (requiresLogin()) {
            return loadConfigAfterLogin(config);
        } else {
            checkServerInfo(config, getCommand());
            applyDefaultOptionValues();
            return loadConfig();
        }
    }

    private ConfigData loadConfigAfterLogin(ConfigData config) {
        ConfigHandler old = ConfigUtil.getHandler();
        try {
            applyDefaultOptionValues();
            initConfigData(config);
            ConfigUtil.setupInMemoryHandler(config);

            BaseConfigCredentialsCmd login = new BaseConfigCredentialsCmd(commandState);
            login.initFromParent(this);
            login.init(config);
            login.process();

            return loadConfig();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            ConfigUtil.setHandler(old);
        }
    }
}
