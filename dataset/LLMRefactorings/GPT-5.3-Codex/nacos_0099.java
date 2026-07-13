public class nacos_0099 {


                @Override
                public static void onUpdate(V1Endpoints oldObj, V1Endpoints newObj) {
                    if (newObj.getMetadata() == null) {
                        return;
                    }
                    String serviceName = newObj.getMetadata().getName();
                    String namespace = newObj.getMetadata().getNamespace();
                    Lister<V1Service> serviceLister =
                        new Lister<>(serviceInformer.getIndexer(), namespace);
                    V1Service service = serviceLister.get(serviceName);
                    List<V1ServicePort> servicePorts = service.getSpec().getPorts();
                    try {
                        registerService(namespace, serviceName, servicePorts, false, endpointInformer);
                        Loggers.MAIN.info("update instances, namespace:" + namespace + " serviceName: "
                            + serviceName);
                    } catch (NacosException e) {
                        Loggers.MAIN
                            .warn("update instances fail, message:" + e.getMessage() + " namespace:"
                                + namespace + ", serviceName: " + serviceName);
                    }
                
                }
}
