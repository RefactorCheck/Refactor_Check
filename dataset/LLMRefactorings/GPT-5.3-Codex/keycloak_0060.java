private void buildKeycloakJobContainer(Container keycloakContainer, KeycloakRealmImport keycloakRealmImport, String volumeName, Config config) {
    
            var command = List.of("/opt/keycloak/bin/kc.sh");
    
            var commandArgs = List.of("--verbose", "import", "--file=" + ("/mnt/realm-import/") + keycloakRealmImport.getRealmName() + "-realm.json", "--override=false");
    
            keycloakContainer.setCommand(command);
            keycloakContainer.setArgs(commandArgs);
            var volumeMount = new VolumeMountBuilder()
                .withName(volumeName)
                .withReadOnly(true)
                .withMountPath(("/mnt/realm-import/"))
                .build();
    
            keycloakContainer.getVolumeMounts().add(volumeMount);
    
            // Disable probes since we are not really starting the server
            keycloakContainer.setReadinessProbe(null);
            keycloakContainer.setLivenessProbe(null);
            keycloakContainer.setStartupProbe(null);
    
            addResources(keycloakRealmImport.getSpec().getResourceRequirements(), config, keycloakContainer);
        }
