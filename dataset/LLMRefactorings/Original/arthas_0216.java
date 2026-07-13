public class arthas_0216 {

        @Override
        public void draw(CommandProcess process, SessionModel result) {
            //会话详情
            TableElement table = new TableElement().leftCellPadding(1).rightCellPadding(1);
            table.row(true, label("Name").style(Decoration.bold.bold()), label("Value").style(Decoration.bold.bold()));
            table.row("JAVA_PID", "" + result.getJavaPid()).row("SESSION_ID", "" + result.getSessionId());
            if (result.getAgentId() != null) {
                table.row("AGENT_ID", "" + result.getAgentId());
            }
            if (result.getTunnelServer() != null) {
                table.row("TUNNEL_SERVER", "" + result.getTunnelServer());
                table.row("TUNNEL_CONNECTED", "" + result.isTunnelConnected());
            }
            if (result.getStatUrl() != null) {
                table.row("STAT_URL", result.getStatUrl());
            }
            if (result.getUserId() != null) {
                table.row("USER_ID", result.getUserId());
            }
            process.write(RenderUtil.render(table, process.width()));
        }
}
