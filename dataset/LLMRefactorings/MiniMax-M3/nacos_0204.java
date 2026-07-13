public class nacos_0204 {

        private List<String> loadSql(String sqlFile) throws Exception {
            List<String> sqlList = new ArrayList<>();
            InputStream sqlFileIn = null;
            try {
                File file = new File(
                    EnvUtil.getNacosHome() + File.separator + "conf" + File.separator
                        + "derby-schema.sql");
                if (StringUtils.isBlank(EnvUtil.getNacosHome()) || !file.exists()) {
                    ClassLoader classLoader = getClass().getClassLoader();
                    URL url = classLoader.getResource(sqlFile);
                    sqlFileIn = url.openStream();
                } else {
                    sqlFileIn = new FileInputStream(file);
                }
                sqlList = parseSqlStatements(readSqlFile(sqlFileIn), sqlList);
                return sqlList;
            } catch (Exception ex) {
                throw new Exception(ex.getMessage());
            } finally {
                IoUtils.closeQuietly(sqlFileIn);
            }
        }

        private StringBuilder readSqlFile(InputStream sqlFileIn) throws IOException {
            StringBuilder sqlSb = new StringBuilder();
            byte[] buff = new byte[1024];
            int byteRead = 0;
            while ((byteRead = sqlFileIn.read(buff)) != -1) {
                sqlSb.append(new String(buff, 0, byteRead, PersistenceConstant.DEFAULT_ENCODE));
            }
            return sqlSb;
        }

        private List<String> parseSqlStatements(StringBuilder sqlSb, List<String> sqlList) {
            String[] sqlArr = sqlSb.toString().split(";");
            for (int i = 0; i < sqlArr.length; i++) {
                String sql = sqlArr[i].replaceAll("--.*", "").trim();
                if (StringUtils.isNotEmpty(sql)) {
                    sqlList.add(sql);
                }
            }
            return sqlList;
        }
}
