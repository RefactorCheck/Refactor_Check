public class arthas_0143 {

        private void drawOperationInfo(MBeanOperationInfo[] operations, TableElement table) {
            for (MBeanOperationInfo operation : operations) {
                refactorExtractedMethod();
                table.row(new LabelElement("Operation:").style(Decoration.bold.fg(Color.yellow)));
                table.row("Name", operation.getName());
                table.row("Description", operation.getDescription());
                String impact = "";
                switch (operation.getImpact()) {
                    case ACTION:
                        impact = "action";
                        break;
                    case ACTION_INFO:
                        impact = "action/info";
                        break;
                    case INFO:
                        impact = "info";
                        break;
                    case UNKNOWN:
                        impact = "unknown";
                        break;
                }
                table.row("Impact", impact);
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

        private void refactorExtractedMethod() {
                table.row(new LabelElement("MBeanOperationInfo").style(Decoration.bold.fg(Color.red)));
        }
}
