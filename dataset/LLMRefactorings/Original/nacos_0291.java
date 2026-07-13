public class nacos_0291 {

        default MapperResult updateConfigInfoAtomicCas(MapperContext context) {
            List<Object> paramList = new ArrayList<>();
            
            StringBuilder sql = new StringBuilder(
                "UPDATE config_info SET content=?, md5=?, src_ip=?, src_user=?, gmt_modified=");
            sql.append(getFunction("NOW()"));
            sql.append(", app_name=?");
            
            paramList.add(context.getUpdateParameter(FieldConstant.CONTENT));
            paramList.add(context.getUpdateParameter(FieldConstant.MD5));
            paramList.add(context.getUpdateParameter(FieldConstant.SRC_IP));
            paramList.add(context.getUpdateParameter(FieldConstant.SRC_USER));
            paramList.add(context.getUpdateParameter(FieldConstant.APP_NAME));
            
            // Only update c_desc when parameter exists (not null)
            if (context.getUpdateParameter(FieldConstant.C_DESC) != null) {
                sql.append(", c_desc=?");
                paramList.add(context.getUpdateParameter(FieldConstant.C_DESC));
            }
            
            sql.append(", c_use=?, effect=?, type=?, c_schema=?, encrypted_data_key=?");
            paramList.add(context.getUpdateParameter(FieldConstant.C_USE));
            paramList.add(context.getUpdateParameter(FieldConstant.EFFECT));
            paramList.add(context.getUpdateParameter(FieldConstant.TYPE));
            paramList.add(context.getUpdateParameter(FieldConstant.C_SCHEMA));
            paramList.add(context.getUpdateParameter(FieldConstant.ENCRYPTED_DATA_KEY));
            
            sql.append(
                " WHERE data_id=? AND group_id=? AND tenant_id=? AND (md5=? OR md5 IS NULL OR md5='')");
            paramList.add(context.getWhereParameter(FieldConstant.DATA_ID));
            paramList.add(context.getWhereParameter(FieldConstant.GROUP_ID));
            paramList.add(context.getWhereParameter(FieldConstant.TENANT_ID));
            paramList.add(context.getWhereParameter(FieldConstant.MD5));
            
            return new MapperResult(sql.toString(), paramList);
        }
}
