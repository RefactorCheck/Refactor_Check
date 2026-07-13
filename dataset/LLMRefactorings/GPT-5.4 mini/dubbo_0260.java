public class dubbo_0260 {

        private static void parseMetrics(Element element, ParserContext parserContext, RootBeanDefinition beanDefinition) {            parseMetricsExtracted(element, parserContext, beanDefinition);
}

public class dubbo_0260 {

        private static void parseMetricsExtracted(Element element, ParserContext parserContext, RootBeanDefinition beanDefinition) {
            NodeList childNodes = element.getChildNodes();
            PrometheusConfig prometheus = null;
            for (int i = 0; i < childNodes.getLength(); i++) {
                if (!(childNodes.item(i) instanceof Element)) {
                    continue;
                }
    
                Element child = (Element) childNodes.item(i);
                if ("aggregation".equals(child.getNodeName()) || "aggregation".equals(child.getLocalName())) {
                    AggregationConfig aggregation = new AggregationConfig();
                    assignProperties(aggregation, child, parserContext);
                    beanDefinition.getPropertyValues().addPropertyValue("aggregation", aggregation);
                } else if ("histogram".equals(child.getNodeName()) || "histogram".equals(child.getLocalName())) {
                    HistogramConfig histogram = new HistogramConfig();
                    assignProperties(histogram, child, parserContext);
                    beanDefinition.getPropertyValues().addPropertyValue("histogram", histogram);
                } else if ("prometheus-exporter".equals(child.getNodeName())
                        || "prometheus-exporter".equals(child.getLocalName())) {
                    if (prometheus == null) {
                        prometheus = new PrometheusConfig();
                    }
    
                    PrometheusConfig.Exporter exporter = new PrometheusConfig.Exporter();
                    assignProperties(exporter, child, parserContext);
                    prometheus.setExporter(exporter);
                } else if ("prometheus-pushgateway".equals(child.getNodeName())
                        || "prometheus-pushgateway".equals(child.getLocalName())) {
                    if (prometheus == null) {
                        prometheus = new PrometheusConfig();
                    }
    
                    PrometheusConfig.Pushgateway pushgateway = new PrometheusConfig.Pushgateway();
                    assignProperties(pushgateway, child, parserContext);
                    prometheus.setPushgateway(pushgateway);
                }
            }
    
            if (prometheus != null) {
                beanDefinition.getPropertyValues().addPropertyValue("prometheus", prometheus);
            }
        
}
}
