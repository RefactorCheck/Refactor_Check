public class dubbo_0005 {

    private void createInjvmInvoker(Exporter<?> exporter) {
        if (injvmInvoker == null) {
            synchronized (createLock) {
                if (injvmInvoker == null) {
                    URL url = createConsumerUrl();
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

    private URL createConsumerUrl() {
        URL url = new ServiceConfigURL(
                LOCAL_PROTOCOL,
                NetUtils.getLocalHost(),
                getUrl().getPort(),
                getInterface().getName(),
                getUrl().getParameters());
        url = url.setScopeModel(getUrl().getScopeModel());
        url = url.setServiceModel(getUrl().getServiceModel());
        return url;
    }
}
