public class keycloak_0060 {

        private void buildKeycloakJobContainer(Container keycloakContainer, KeycloakRealmImport keycloakRealmImport, String volumeName, Config config) {
            var importMntPath = "/mnt/realm-import/";

            var command = List.of("/opt/keycloak/bin/kc.sh");

            String realmImportFile = importMntPath + keycloakRealmImport.getRealmName() + "-realm.json";
            var commandArgs = List.of("--verbose", "import", "--file=" + realmImportFile, "--override=false");

            keycloakContainer.setCommand(command);
            keycloakContainer.setArgs(commandArgs);
            var volumeMount = new VolumeMountBuilder()
                .withName(volumeName)
                .withReadOnly(true)
                .withMountPath(importMntPath)
                .build();

            keycloakContainer.getVolumeMounts().add(volumeMount);

            // Disable probes since we are not really starting the server
            keycloakContainer.setReadinessProbe(null);
            keycloakContainer.setLivenessProbe(null);
            keycloakContainer.setStartupProbe(null);

            addResources(keycloakRealmImport.getSpec().getResourceRequirements(), config, keycloakContainer);
        }
}
