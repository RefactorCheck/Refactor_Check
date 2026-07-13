public class dubbo_0260 {

        private static void parseMetrics(Element element, ParserContext parserContext, RootBeanDefinition beanDefinition) {
            NodeList childNodes = element.getChildNodes();
            PrometheusConfig prometheus = null;
            for (int i = 0; i < childNodes.getLength(); i++) {
                if (!(childNodes.item(i) instanceof Element)) {
                    continue;
                }
    
                Element child = (Element) childNodes.item(i);
                if (matchesNode(child, "aggregation")) {
                    AggregationConfig aggregation = new AggregationConfig();
                    assignProperties(aggregation, child, parserContext);
                    beanDefinition.getPropertyValues().addPropertyValue("aggregation", aggregation);
                } else if (matchesNode(child, "histogram")) {
                    HistogramConfig histogram = new HistogramConfig();
                    assignProperties(histogram, child, parserContext);
                    beanDefinition.getPropertyValues().addPropertyValue("histogram", histogram);
                } else if (matchesNode(child, "prometheus-exporter")) {
                    if (prometheus == null) {
                        prometheus = new PrometheusConfig();
                    }
    
                    PrometheusConfig.Exporter exporter = new PrometheusConfig.Exporter();
                    assignProperties(exporter, child, parserContext);
                    prometheus.setExporter(exporter);
                } else if (matchesNode(child, "prometheus-pushgateway")) {
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

        private static boolean matchesNode(Element element, String name) {
            return name.equals(element.getNodeName()) || name.equals(element.getLocalName());
        }
}
