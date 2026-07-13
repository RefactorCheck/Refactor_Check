public class arthas_0287 {

        public static String welcome(Map<String, String> infos) {
            logger.info("Current arthas version: {}, recommend latest version: {}", version(), latestVersion());
            String appName = getAppName();
            TableElement table = new TableElement().rightCellPadding(1)
                            .row("wiki", wiki())
                            .row("tutorials", tutorials())
                            .row("version", version())
                            .row("main_class", PidUtils.mainClass());
    
            if (appName != null) {
                table.row("app_name", appName);
            }
            table.row("pid", PidUtils.currentPid())
                 .row("start_time", DateUtils.getStartDateTime())
                 .row("current_time", DateUtils.getCurrentDateTime());
            for (Entry<String, String> entry : infos.entrySet()) {
                table.row(entry.getKey(), entry.getValue());
            }
    
            return logo() + "\n" + RenderUtil.render(table);
        }

        private static String getAppName() {
            String appName = System.getProperty("project.name");
            if (appName == null) {
                appName = System.getProperty("app.name");
            }
            if (appName == null) {
                appName = System.getProperty("spring.application.name");
            }
            return appName;
        }
}
