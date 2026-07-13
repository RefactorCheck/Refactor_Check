public class arthas_0010 {

    @Override
    public void protocolCommandSent(ProtocolCommandEvent event)
    {
        if (__directionMarker) {
            __writer.print("> ");
        }
        writeCommandMessage(event);
        __writer.flush();
    }

    private void writeCommandMessage(ProtocolCommandEvent event) {
        if (__nologin) {
            String cmd = event.getCommand();
            if ("PASS".equalsIgnoreCase(cmd) || "USER".equalsIgnoreCase(cmd)) {
                __writer.print(cmd);
                __writer.println(" *******");
            } else {
                final String IMAP_LOGIN = "LOGIN";
                if (IMAP_LOGIN.equalsIgnoreCase(cmd)) {
                    String msg = event.getMessage();
                    msg=msg.substring(0, msg.indexOf(IMAP_LOGIN)+IMAP_LOGIN.length());
                    __writer.print(msg);
                    __writer.println(" *******");
                } else {
                    __writer.print(getPrintableString(event.getMessage()));
                }
            }
        } else {
            __writer.print(getPrintableString(event.getMessage()));
        }
    }
}
