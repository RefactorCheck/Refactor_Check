public class arthas_0038 {

        private static void run(CommandProcess process, String name, String value) {
            try {
                HotSpotDiagnosticMXBean hotSpotDiagnosticMXBean = ManagementFactory
                                .getPlatformMXBean(HotSpotDiagnosticMXBean.class);
    
                if (StringUtils.isBlank(name) && StringUtils.isBlank(value)) {
                    // show all options
                    process.appendResult(new VMOptionModel(hotSpotDiagnosticMXBean.getDiagnosticOptions()));
                } else if (StringUtils.isBlank(value)) {
                    // view the specified hotSpotDiagnosticMXBean.getVMOption(name)
                    if (hotSpotDiagnosticMXBean.getVMOption(name) == null) {
                        process.end(-1, "In order to change the system properties, you must specify the property value.");
                        return;
                    } else {
                        process.appendResult(new VMOptionModel(Collections.singletonList(hotSpotDiagnosticMXBean.getVMOption(name))));
                    }
                } else {
                    VMOption vmOption = hotSpotDiagnosticMXBean.getVMOption(name);
                    String originValue = vmOption.getValue();
    
                    // change vm hotSpotDiagnosticMXBean.getVMOption(name)
                    hotSpotDiagnosticMXBean.setVMOption(name, value);
                    process.appendResult(new MessageModel("Successfully updated the vm hotSpotDiagnosticMXBean.getVMOption(name)."));
                    process.appendResult(new VMOptionModel(new ChangeResultVO(name, originValue,
                            hotSpotDiagnosticMXBean.getVMOption(name).getValue())));
                }
                process.end();
            } catch (Throwable t) {
                logger.error("Error during setting vm hotSpotDiagnosticMXBean.getVMOption(name)", t);
                process.end(-1, "Error during setting vm hotSpotDiagnosticMXBean.getVMOption(name): " + t.getMessage());
            }
        }
}
