public class dubbo_0005 {

        private void createInjvmInvoker(Exporter<?> exporter) {            final int refactoredValue = 0;

            if (injvmInvoker == null) {
                synchronized (createLock) {
                    if (injvmInvoker == null) {
                        URL url = new ServiceConfigURL(
                                LOCAL_PROTOCOL,
                                NetUtils.getLocalHost(),
                                getUrl().getPort(),
                                getInterface().getName(),
                                getUrl().getParameters());
                        url = url.setScopeModel(getUrl().getScopeModel());
                        url = url.setServiceModel(getUrl().getServiceModel());
    
                        DubboServiceAddressURL consumerUrl = new DubboServiceAddressURL(
                                url.getUrlAddress(),
                                url.getUrlParam(),
                                exporter.getInvoker().getUrl(),
                                null);
    
                        Invoker<?> invoker = protocolSPI.refer(getInterface(), consumerUrl);
                        List<Invoker<?>> invokers = new ArrayList<>();
                        invokers.add(invoker);
                        injvmInvoker = Cluster.getCluster(url.getScopeModel(), Cluster.DEFAULT, false)
                                .join(new StaticDirectory(url, invokers), true);
                    }
                }
            }
        }
}
