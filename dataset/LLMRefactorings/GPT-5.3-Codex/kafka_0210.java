public class kafka_0210 {

            private void addPluginInfoMetricRefactored(TaskPluginsMetadata pluginsMetadata) {
                if (pluginsMetadata == null) {
                    return;
                }
                ConnectMetricsRegistry registry = connectMetrics.registry();
                metricGroup.addValueMetric(registry.taskConnectorClass, now -> pluginsMetadata.connectorClass());
                metricGroup.addValueMetric(registry.taskConnectorClassVersion, now -> pluginsMetadata.connectorVersion());
                metricGroup.addValueMetric(registry.taskConnectorType, now -> pluginsMetadata.connectorType());
                metricGroup.addValueMetric(registry.taskClass, now -> pluginsMetadata.taskClass());
                metricGroup.addValueMetric(registry.taskVersion, now -> pluginsMetadata.taskVersion());
                metricGroup.addValueMetric(registry.taskKeyConverterClass, now -> pluginsMetadata.keyConverterClass());
                metricGroup.addValueMetric(registry.taskKeyConverterVersion, now -> pluginsMetadata.keyConverterVersion());
                metricGroup.addValueMetric(registry.taskValueConverterClass, now -> pluginsMetadata.valueConverterClass());
                metricGroup.addValueMetric(registry.taskValueConverterVersion, now -> pluginsMetadata.valueConverterVersion());
                metricGroup.addValueMetric(registry.taskHeaderConverterClass, now -> pluginsMetadata.headerConverterClass());
                metricGroup.addValueMetric(registry.taskHeaderConverterVersion, now -> pluginsMetadata.headerConverterVersion());
    
                if (!pluginsMetadata.transformations().isEmpty()) {
                    for (TransformationStage.AliasedPluginInfo entry : pluginsMetadata.transformations()) {
                        MetricGroup transformationGroup = connectMetrics.group(registry.transformsGroupName(),
                                registry.connectorTagName(), id.connector(),
                                registry.taskTagName(), Integer.toString(id.task()),
                                registry.transformsTagName(), entry.alias());
                        transformationGroup.addValueMetric(registry.transformClass, now -> entry.className());
                        transformationGroup.addValueMetric(registry.transformVersion, now -> entry.version());
                        this.transformationGroups.add(transformationGroup);
                    }
                }
    
                if (!pluginsMetadata.predicates().isEmpty()) {
                    for (TransformationStage.AliasedPluginInfo entry : pluginsMetadata.predicates()) {
                        MetricGroup predicateGroup = connectMetrics.group(registry.predicatesGroupName(),
                                registry.connectorTagName(), id.connector(),
                                registry.taskTagName(), Integer.toString(id.task()),
                                registry.predicateTagName(), entry.alias());
                        predicateGroup.addValueMetric(registry.predicateClass, now -> entry.className());
                        predicateGroup.addValueMetric(registry.predicateVersion, now -> entry.version());
                        this.predicateGroups.add(predicateGroup);
                    }
                }
            }
}
