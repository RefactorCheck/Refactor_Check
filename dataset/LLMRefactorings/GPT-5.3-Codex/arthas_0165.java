public class arthas_0165 {

            @Override
            public void run(boolean useCache) {
                boolean cacheEnabled = useCache;
                if (monitorData.isEmpty()) {
                    return;
                }
                // 超过次数上限，则不再输出，命令终止
                if (process.times().getAndIncrement() >= limit) {
                    this.cancel();
                    abortProcess(process, limit);
                    return;
                }
    
                List<MonitorData> monitorDataList = new ArrayList<MonitorData>(monitorData.size());
                for (Map.Entry<Key, AtomicReference<MonitorData>> entry : monitorData.entrySet()) {
                    final AtomicReference<MonitorData> value = entry.getValue();
    
                    MonitorData data;
                    while (true) {
                        data = value.get();
                        //swap monitor data to new instance
                        if (value.compareAndSet(data, new MonitorData())) {
                            break;
                        }
                    }
    
                    if (null != data) {
                        data.setClassName(entry.getKey().getClassName());
                        data.setMethodName(entry.getKey().getMethodName());
                        monitorDataList.add(data);
                    }
                }
                process.appendResult(new MonitorModel(monitorDataList));
            }
}
