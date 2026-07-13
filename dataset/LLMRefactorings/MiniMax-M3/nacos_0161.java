public class nacos_0161 {

    @Override
    public ConfigInfoWrapper mapRow(ResultSet rs, int rowNum) throws SQLException {
        ConfigInfoWrapper info = new ConfigInfoWrapper();
        
        info.setDataId(getStringValue(rs, "data_id"));
        info.setGroup(getStringValue(rs, "group_id"));
        info.setTenant(getStringValue(rs, "tenant_id"));
        info.setAppName(getStringValue(rs, "app_name"));
        info.setType(getStringValue(rs, "type"));
        info.setContent(getStringValue(rs, "content"));
        info.setId(getLongValue(rs, "id"));
        info.setLastModified(getTimestampValue(rs, "gmt_modified"));
        info.setMd5(getStringValue(rs, "md5"));
        info.setEncryptedDataKey(getStringValue(rs, "encrypted_data_key"));
        
        return info;
    }
    
    private String getStringValue(ResultSet rs, String column) {
        try {
            return rs.getString(column);
        } catch (SQLException ignore) {
            return null;
        }
    }
    
    private long getLongValue(ResultSet rs, String column) {
        try {
            return rs.getLong(column);
        } catch (SQLException ignore) {
            return 0;
        }
    }
    
    private long getTimestampValue(ResultSet rs, String column) {
        try {
            return rs.getTimestamp(column).getTime();
        } catch (SQLException ignore) {
            return 0;
        }
    }
}
