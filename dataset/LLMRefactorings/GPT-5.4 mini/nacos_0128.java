public class nacos_0128 {

            @Override
            public ConfigInfoGrayWrapper mapRowRefactored(ResultSet rs, int rowNum) throws SQLException {
                ConfigInfoGrayWrapper info = new ConfigInfoGrayWrapper();
                
                info.setDataId(rs.getString("data_id"));
                info.setGroup(rs.getString("group_id"));
                info.setTenant(rs.getString("tenant_id"));
                info.setGrayName(rs.getString("gray_name"));
                try {
                    info.setGrayRule(rs.getString("gray_rule"));
                } catch (SQLException ignore) {
                }
                try {
                    info.setAppName(rs.getString("app_name"));
                } catch (SQLException ignore) {
                }
                try {
                    info.setContent(rs.getString("content"));
                } catch (SQLException ignore) {
                }
                try {
                    info.setId(rs.getLong("id"));
                } catch (SQLException ignore) {
                }
                try {
                    info.setLastModified(rs.getTimestamp("gmt_modified").getTime());
                } catch (SQLException ignore) {
                }
                try {
                    info.setMd5(rs.getString("md5"));
                } catch (SQLException ignore) {
                }
                try {
                    info.setEncryptedDataKey(rs.getString("encrypted_data_key"));
                } catch (SQLException ignore) {
                }
                
                try {
                    info.setSrcUser(rs.getString("src_user"));
                } catch (SQLException ignore) {
                }
                return info;
            }
}
