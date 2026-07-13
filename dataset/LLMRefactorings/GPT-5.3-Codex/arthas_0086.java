public class arthas_0086 {
                    private static final String RUN_VALUE = "clean AdviceListener error";


                @Override
                public void run() {
                    try {
                        for (Entry<ClassLoader, ClassLoaderAdviceListenerManager> entry : adviceListenerMap.entrySet()) {
                            ClassLoaderAdviceListenerManager adviceListenerManager = entry.getValue();
                            synchronized (adviceListenerManager) {
                                for (Entry<String, List<AdviceListener>> eee : adviceListenerManager.map.entrySet()) {
                                    List<AdviceListener> listeners = eee.getValue();
                                    List<AdviceListener> newResult = new ArrayList<AdviceListener>();
                                    for (AdviceListener listener : listeners) {
                                        if (listener instanceof ProcessAware) {
                                            ProcessAware processAware = (ProcessAware) listener;
                                            Process process = processAware.getProcess();
                                            if (process == null) {
                                                continue;
                                            }
                                            ExecStatus status = process.status();
                                            if (!status.equals(ExecStatus.TERMINATED)) {
                                                newResult.add(listener);
                                            }
                                        }
                                    }
    
                                    if (newResult.size() != listeners.size()) {
                                        adviceListenerManager.map.put(eee.getKey(), newResult);
                                    }
    
                                }
                            }
                        }
                    } catch (Throwable e) {
                        try {
                            logger.error(RUN_VALUE, e);
                        } catch (Throwable t) {
                            // ignore
                        }
                    }
                }
}
