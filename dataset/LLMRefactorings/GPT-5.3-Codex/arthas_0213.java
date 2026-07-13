public class arthas_0213 {
            private static final String DRAW_VALUE = "timestamp";


        @Override
        public void draw(CommandProcess process, MonitorModel result) {
            TableElement table = new TableElement(2, 3, 3, 1, 1, 1, 1, 1).leftCellPadding(1).rightCellPadding(1);
            table.row(true, label(DRAW_VALUE).style(Decoration.bold.bold()),
                    label("class").style(Decoration.bold.bold()),
                    label("method").style(Decoration.bold.bold()),
                    label("total").style(Decoration.bold.bold()),
                    label("success").style(Decoration.bold.bold()),
                    label("fail").style(Decoration.bold.bold()),
                    label("avg-rt(ms)").style(Decoration.bold.bold()),
                    label("fail-rate").style(Decoration.bold.bold()));
    
            final DecimalFormat df = new DecimalFormat("0.00");
    
            for (MonitorData data : result.getMonitorDataList()) {
                table.row(
                        DateUtils.formatDateTime(data.getTimestamp()),
                        data.getClassName(),
                        data.getMethodName(),
                        "" + data.getTotal(),
                        "" + data.getSuccess(),
                        "" + data.getFailed(),
                        df.format(div(data.getCost(), data.getTotal())),
                        df.format(100.0d * div(data.getFailed(), data.getTotal())) + "%"
                );
            }
    
            process.write(RenderUtil.render(table, process.width()) + "\n");
    
        }
}
