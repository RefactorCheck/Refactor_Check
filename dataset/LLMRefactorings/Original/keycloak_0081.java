public class keycloak_0081 {

        protected ConfigData ensureAuthInfo(ConfigData config) {
    
            if (requiresLogin()) {
                // make sure current handler is in-memory handler
                // restore it at the end
                ConfigHandler old = ConfigUtil.getHandler();
                try {
                    // make sure all defaults are initialized after this point
                    applyDefaultOptionValues();
    
                    initConfigData(config);
                    ConfigUtil.setupInMemoryHandler(config);
    
                    BaseConfigCredentialsCmd login = new BaseConfigCredentialsCmd(commandState);
                    login.initFromParent(this);
                    login.init(config);
                    login.process();
    
                    // this must be executed before finally block which restores config handler
                    return loadConfig();
    
                } catch (RuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    ConfigUtil.setHandler(old);
                }
    
            } else {
                checkServerInfo(config, getCommand());
    
                // make sure all defaults are initialized after this point
                applyDefaultOptionValues();
                return loadConfig();
            }
        }
}
