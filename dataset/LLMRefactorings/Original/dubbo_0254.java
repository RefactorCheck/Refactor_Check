public class dubbo_0254 {

        private boolean isDecodeDataInIoThread(Channel channel) {
            Object obj = channel.getAttribute(DECODE_IN_IO_THREAD_KEY);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
    
            String mode = ExecutorRepository.getMode(channel.getUrl().getOrDefaultApplicationModel());
            boolean isIsolated = EXECUTOR_MANAGEMENT_MODE_ISOLATION.equals(mode);
    
            if (isIsolated && !decodeInUserThreadLogged.compareAndSet(false, true)) {
                channel.setAttribute(DECODE_IN_IO_THREAD_KEY, true);
                return true;
            }
    
            boolean decodeDataInIoThread =
                    channel.getUrl().getParameter(DECODE_IN_IO_THREAD_KEY, DEFAULT_DECODE_IN_IO_THREAD);
            if (isIsolated && !decodeDataInIoThread) {
                log.info("Because thread pool isolation is enabled on the dubbo protocol, the body can only be decoded "
                        + "on the io thread, and the parameter[" + DECODE_IN_IO_THREAD_KEY + "] will be ignored");
                // Why? because obtaining the isolated thread pool requires the serviceKey of the service,
                // and this part must be decoded before it can be obtained (more see DubboExecutorSupport)
                channel.setAttribute(DECODE_IN_IO_THREAD_KEY, true);
                return true;
            }
            channel.setAttribute(DECODE_IN_IO_THREAD_KEY, decodeDataInIoThread);
            return decodeDataInIoThread;
        }
}
