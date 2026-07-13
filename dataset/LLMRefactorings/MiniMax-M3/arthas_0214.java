public class arthas_0214 {

        public static String drawThreadInfo(List<ThreadVO> threads, int width, int height) {
            TableElement table = new TableElement(1, 6, 3, 2, 2, 2, 2, 2, 2, 2).overflow(Overflow.HIDDEN).rightCellPadding(1);
    
            table.add(
                    new RowElement().style(Decoration.bold.fg(Color.black).bg(Color.white)).add(
                            "ID",
                            "NAME",
                            "GROUP",
                            "PRIORITY",
                            "STATE",
                            "%CPU",
                            "DELTA_TIME",
                            "TIME",
                            "INTERRUPTED",
                            "DAEMON"
                    )
            );
    
            int count = 0;
            for (ThreadVO thread : threads) {
                Color color = colorMapping.get(thread.getState());
                String time = formatTimeMills(thread.getTime());
                String deltaTime = formatTimeMillsToSeconds(thread.getDeltaTime());
                double cpu = thread.getCpu();
    
                LabelElement daemonLabel = new LabelElement(thread.isDaemon());
                if (!thread.isDaemon()) {
                    daemonLabel.setStyle(Style.style(Color.magenta));
                }
                table.row(
                        new LabelElement(thread.getId()),
                        new LabelElement(thread.getName()),
                        new LabelElement(thread.getGroup() != null ? thread.getGroup() : "-"),
                        new LabelElement(thread.getPriority()),
                        createStateElement(thread, color),
                        new LabelElement(cpu),
                        new LabelElement(deltaTime),
                        new LabelElement(time),
                        new LabelElement(thread.isInterrupted()),
                        daemonLabel
                );
                if (++count >= height) {
                    break;
                }
            }
            return RenderUtil.render(table, width, height);
        }

        private static LabelElement createStateElement(ThreadVO thread, Color color) {
            if (thread.getState() != null) {
                return new LabelElement(thread.getState()).style(color.fg());
            }
            return new LabelElement("-");
        }
}
