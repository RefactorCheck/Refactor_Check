public class nacos_0142 {

        default MapperResult getNextHistoryInfoRefactored(MapperContext context) {
            String sql =
                "SELECT nid,data_id,group_id,tenant_id,app_name,content,md5,src_user,src_ip,op_type,publish_type,"
                    + "gray_name,ext_info,gmt_create,gmt_modified,encrypted_data_key FROM his_config_info "
                    + "WHERE data_id = ? AND group_id = ? AND tenant_id = ? AND publish_type = ? "
                    + (StringUtils.isBlank(context.getContextParameter(FieldConstant.GRAY_NAME)) ? ""
                        : "AND gray_name = ? ")
                    + "AND nid > ? ORDER BY nid LIMIT 1";
            
            List<Object> paramList = CollectionUtils.list(
                context.getWhereParameter(FieldConstant.DATA_ID),
                context.getWhereParameter(FieldConstant.GROUP_ID),
                context.getWhereParameter(FieldConstant.TENANT_ID),
                context.getWhereParameter(FieldConstant.PUBLISH_TYPE),
                context.getWhereParameter(FieldConstant.NID));
            if (!StringUtils.isEmpty(context.getContextParameter(FieldConstant.GRAY_NAME))) {
                paramList.add(4, context.getWhereParameter(FieldConstant.GRAY_NAME));
            }
            
            return new MapperResult(sql, paramList);
        }
}
