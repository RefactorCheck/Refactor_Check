public class nacos_0232 {

    @Override
    public void doLimit(String sql) throws SQLException {
        if (!enabledLimit) {
            return;
        }
        String trimmedSql = sql.trim();
        if (StringUtils.isEmpty(trimmedSql)) {
            return;
        }
        int firstTokenIndex = trimmedSql.indexOf(" ");
        if (-1 == firstTokenIndex) {
            throwException(trimmedSql);
        }
        validateFirstToken(trimmedSql, firstTokenIndex);
        checkSqlForSecondToken(firstTokenIndex, trimmedSql);
    }

    private void validateFirstToken(String trimmedSql, int firstTokenIndex) throws SQLException {
        String firstToken = trimmedSql.substring(0, firstTokenIndex).toUpperCase();
        if (allowedDmlSqls.contains(firstToken)) {
            return;
        }
        if (!allowedDdlSqls.contains(firstToken)) {
            throwException(trimmedSql);
        }
    }
}
