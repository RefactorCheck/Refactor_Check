public class arthas_0040 {

        @Override
        public void draw(CommandProcess process, PerfCounterModel result) {
            List<PerfCounterVO> perfCounters = result.getPerfCounters();
            boolean details = result.isDetails();
            TableElement table;
            if (details) {
                table = new TableElement(3, 1, 1, 10).leftCellPadding(1).rightCellPadding(1);
                table.row(true, boldLabel("Name"), boldLabel("Variability"),
                        boldLabel("Units"), boldLabel("Value"));
            } else {
                table = new TableElement(4, 6).leftCellPadding(1).rightCellPadding(1);
                table.row(true, boldLabel("Name"), boldLabel("Value"));
            }
    
            for (PerfCounterVO counter : perfCounters) {
                if (details) {
                    table.row(counter.getName(), counter.getVariability(),
                            counter.getUnits(), String.valueOf(counter.getValue()));
                } else {
                    table.row(counter.getName(), String.valueOf(counter.getValue()));
                }
            }
            process.write(RenderUtil.render(table, process.width()));
        }
    
        private LabelElement boldLabel(String text) {
            return label(text).style(Decoration.bold.bold());
        }
}
