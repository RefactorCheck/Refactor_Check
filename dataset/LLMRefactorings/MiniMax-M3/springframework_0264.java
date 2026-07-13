public class springframework_0264 {

    @Override
    public void releaseSavepoint(Object savepoint) throws TransactionException {
        ConnectionHolder conHolder = getConnectionHolderForSavepoint();
        try {
            conHolder.getConnection().releaseSavepoint((Savepoint) savepoint);
        }
        catch (SQLFeatureNotSupportedException ex) {
            // typically on Oracle - ignore
        }
        catch (SQLException ex) {
            handleSqlException(ex);
        }
        catch (Throwable ex) {
            throw new TransactionSystemException("Could not explicitly release JDBC savepoint", ex);
        }
    }

    private void handleSqlException(SQLException ex) throws TransactionException {
        if ("3B001".equals(ex.getSQLState())) {
            // Savepoint already released (HSQLDB, PostgreSQL, DB2) - ignore
            return;
        }
        // ignore Microsoft SQLServerException: This operation is not supported.
        String msg = ex.getMessage();
        if (msg == null || (!msg.contains("not supported") && !msg.contains("3B001"))) {
            throw new TransactionSystemException("Could not explicitly release JDBC savepoint", ex);
        }
    }
}
