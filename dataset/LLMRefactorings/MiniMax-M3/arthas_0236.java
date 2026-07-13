public class arthas_0236 {

    public static void drawPlayException(TableElement table, ObjectVO throwableVO) {
        table.row("IS-RETURN", "" + false);
        table.row("IS-EXCEPTION", "" + true);

        Throwable cause;
        Throwable t = (Throwable) throwableVO.getObject();
        if (t instanceof InvocationTargetException) {
            cause = t.getCause();
        } else {
            cause = t;
        }

        if (throwableVO.needExpand()) {
            table.row("THROW-EXCEPTION", new ObjectView(cause, throwableVO.expandOrDefault()).draw());
        } else {
            table.row("THROW-EXCEPTION", getStackTrace(cause));
        }
    }

    private static String getStackTrace(Throwable cause) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        try {
            cause.printStackTrace(printWriter);
            return stringWriter.toString();
        } finally {
            printWriter.close();
        }
    }
}
