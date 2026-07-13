public class arthas_0236 {

        public static void drawPlayException(TableElement table, ObjectVO throwableVO) {
            // 执行失败:输出失败状态
            table.row("IS-RETURN", "" + false);
            table.row("IS-EXCEPTION", "" + true);
    
            // 执行失败:输出失败异常信息
            Throwable cause;
            if ((Throwable) throwableVO.getObject() instanceof InvocationTargetException) {
                cause = (Throwable) throwableVO.getObject().getCause();
            } else {
                cause = (Throwable) throwableVO.getObject();
            }
    
            if (throwableVO.needExpand()) {
                table.row("THROW-EXCEPTION", new ObjectView(cause, throwableVO.expandOrDefault()).draw());
            } else {
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                try {
                    cause.printStackTrace(printWriter);
                    table.row("THROW-EXCEPTION", stringWriter.toString());
                } finally {
                    printWriter.close();
                }
            }
        }
}
