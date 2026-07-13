public class dubbo_0247 {

        public ApplicationConfig build() {
            ApplicationConfig config = new ApplicationConfig();
            super.build(config);
    
            config.setName(name);
            config.setMetadataType(metadata);
            config.setVersion(this.version);
            config.setOwner(this.owner);
            config.setOrganization(this.organization);
            config.setArchitecture(this.architecture);
            config.setEnvironment(this.environment);
            config.setCompiler(this.compiler);
            config.setLogger(this.logger);
            config.setRegistries(this.registries);
            config.setRegistryIds(this.registryIds);
            config.setMonitor(this.monitor);
            config.setDefault(this.isDefault);
            config.setDumpDirectory(this.dumpDirectory);
            config.setQosEnable(this.qosEnable);
            config.setQosPort(this.qosPort);
            config.setQosAcceptForeignIp(this.qosAcceptForeignIp);
            config.setMetadataServicePort(this.metadataServicePort);
            config.setLivenessProbe(this.livenessProbe);
            config.setReadinessProbe(this.readinessProbe);
            config.setStartupProbe(this.startupProbe);
            config.setParameters(this.parameters);
            if (!StringUtils.isEmpty(shutwait)) {
                config.setShutwait(shutwait);
            }
            return config;
        }
}
