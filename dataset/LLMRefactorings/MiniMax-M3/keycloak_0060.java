public class keycloak_0060 {

    private static final String REALM_IMPORT_MOUNT_PATH = "/mnt/realm-import/";
    private static final String KEYCLOAK_START_COMMAND = "/opt/keycloak/bin/kc.sh";
    private static final String VERBOSE_FLAG = "--verbose";
    private static final String IMPORT_COMMAND = "import";
    private static final String FILE_FLAG_PREFIX = "--file=";
    private static final String REALM_FILE_SUFFIX = "-realm.json";
    private static final String OVERRIDE_FLAG = "--override=false";

    private void buildKeycloakJobContainer(Container keycloakContainer, KeycloakRealmImport keycloakRealmImport, String volumeName, Config config) {
        var importMntPath = REALM_IMPORT_MOUNT_PATH;

        var command = List.of(KEYCLOAK_START_COMMAND);

        var commandArgs = List.of(VERBOSE_FLAG, IMPORT_COMMAND, FILE_FLAG_PREFIX + importMntPath + keycloakRealmImport.getRealmName() + REALM_FILE_SUFFIX, OVERRIDE_FLAG);

        keycloakContainer.setCommand(command);
        keycloakContainer.setArgs(commandArgs);
        var volumeMount = new VolumeMountBuilder()
            .withName(volumeName)
            .withReadOnly(true)
            .withMountPath(importMntPath)
            .build();

        keycloakContainer.getVolumeMounts().add(volumeMount);

        keycloakContainer.setReadinessProbe(null);
        keycloakContainer.setLivenessProbe(null);
        keycloakContainer.setStartupProbe(null);

        addResources(keycloakRealmImport.getSpec().getResourceRequirements(), config, keycloakContainer);
    }
}
