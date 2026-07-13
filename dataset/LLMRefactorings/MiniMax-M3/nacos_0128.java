public class nacos_0128 {

    private String getStringSafely(ResultSet rs, String columnName) {
        try {
            return rs.getString(columnName);
        } catch (SQLException ignore) {
            return null;
        }
    }

    @Override
    public ConfigInfoGrayWrapper mapRow(ResultSet rs, int rowNum) throws SQLException {
        ConfigInfoGrayWrapper info = new ConfigInfoGrayWrapper();
        
        info.setDataId(rs.getString("data_id"));
        info.setGroup(rs.getString("group_id"));
        info.setTenant(rs.getString("tenant_id"));
        info.setGrayName(rs.getString("gray_name"));
        info.setGrayRule(getStringSafely(rs, "gray_rule"));
        info.setAppName(getStringSafely(rs, "app_name"));
        info.setContent(getStringSafely(rs, "content"));
        info.setId(rs.getLong("id"));
        try {
            info.setLastModified(rs.getTimestamp("gmt_modified").getTime());
        } catch (SQLException ignore) {
        }
        info.setMd5(getStringSafely(rs, "md5"));
        info.setEncryptedDataKey(getStringSafely(rs, "encrypted_data_key"));
        info.setSrcUser(getStringSafely(rs, "src_user"));
        
        return info;
    }
}
