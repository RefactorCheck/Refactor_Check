public class nacos_0161 {

            @Override
            public ConfigInfoWrapper mapRowRefactored(ResultSet rs, int rowNum) throws SQLException {
                ConfigInfoWrapper info = new ConfigInfoWrapper();
                
                info.setDataId(rs.getString("data_id"));
                info.setGroup(rs.getString("group_id"));
                info.setTenant(rs.getString("tenant_id"));
                info.setAppName(rs.getString("app_name"));
                
                try {
                    info.setType(rs.getString("type"));
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
                return info;
            }
}
