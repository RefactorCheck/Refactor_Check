public class nacos_0056 {


        default void appendOrConditionsRefactored(WhereBuilder where, Map<String, Object> orMap) {
            where.and().startParentheses();
            boolean appended = false;
            for (Map.Entry<String, Object> each : orMap.entrySet()) {
                String field = each.getKey();
                Object value = each.getValue();
                if (StringUtils.isBlank(field) || value == null) {
                    continue;
                }
                if (appended) {
                    where.or();
                }
                if (value instanceof List) {
                    if (((List<?>) value).isEmpty()) {
                        continue;
                    }
                    where.in(field, ((List<?>) value).toArray());
                } else {
                    where.eq(field, value);
                }
                appended = true;
            }
            if (!appended) {
                where.eq("1", 0);
            }
            where.endParentheses();
        
        }
}
