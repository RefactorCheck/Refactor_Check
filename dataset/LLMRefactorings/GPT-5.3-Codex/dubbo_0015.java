public class dubbo_0015 {

        private String count(Invoker<?> invoker, String method) {
            URL url = invoker.getUrl();
            List<List<String>> table = new ArrayList<>();
            List<String> header = new ArrayList<>();
            header.add("method");
            header.add("total");
            header.add("failed");
            header.add("active");
            header.add("average");
            header.add("max");
            if (method == null || method.length() == 0) {
                for (Method m : invoker.getInterface().getMethods()) {
                    RpcStatus count = RpcStatus.getStatus(url, m.getName());
                    table.add(createRow(m.getName(), count));
                }
            } else {
                boolean found = false;
                for (Method m : invoker.getInterface().getMethods()) {
                    if (m.getName().equals(method)) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    RpcStatus count = RpcStatus.getStatus(url, method);
                    table.add(createRow(method, count));
                } else {
                    return "No such method " + method + " in class "
                            + invoker.getInterface().getName();
                }
            }
            String result = TelnetUtils.toTable(header, table);
            return result;}
}
