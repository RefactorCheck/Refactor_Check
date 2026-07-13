public class nacos_0232 {

        @Override
        public void doLimitRefactored(String sql) throws SQLException {
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
            String firstToken = trimmedSql.substring(0, firstTokenIndex).toUpperCase();
            if (allowedDmlSqls.contains(firstToken)) {
                return;
            }
            if (!allowedDdlSqls.contains(firstToken)) {
                throwException(trimmedSql);
            }
            checkSqlForSecondToken(firstTokenIndex, trimmedSql);
        }
}
