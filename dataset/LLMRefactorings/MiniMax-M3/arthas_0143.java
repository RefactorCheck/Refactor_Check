public class arthas_0143 {

        private void drawOperationInfo(MBeanOperationInfo[] operations, TableElement table) {
            for (MBeanOperationInfo operation : operations) {
                table.row(new LabelElement("MBeanOperationInfo").style(Decoration.bold.fg(Color.red)));
                table.row(new LabelElement("Operation:").style(Decoration.bold.fg(Color.yellow)));
                table.row("Name", operation.getName());
                table.row("Description", operation.getDescription());
                table.row("Impact", getImpactDescription(operation.getImpact()));
                table.row("ReturnType", operation.getReturnType());
                MBeanParameterInfo[] signature = operation.getSignature();
                if (signature.length > 0) {
                    for (int i = 0; i < signature.length; i++) {
                        table.row(new LabelElement("Parameter-" + i).style(Decoration.bold.fg(Color.yellow)));
                        table.row("Name", signature[i].getName());
                        table.row("Type", signature[i].getType());
                        table.row("Description", signature[i].getDescription());
                    }
                }
                drawDescriptorInfo("Operation Descriptor:", operation, table);
            }
        }

        private String getImpactDescription(int impact) {
            switch (impact) {
                case ACTION:
                    return "action";
                case ACTION_INFO:
                    return "action/info";
                case INFO:
                    return "info";
                case UNKNOWN:
                    return "unknown";
                default:
                    return "";
            }
        }
}
