public class nacos_0056 {

        default void appendOrConditions(WhereBuilder where, Map<String, Object> orMap) {
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
                if (appendCondition(where, field, value)) {
                    appended = true;
                }
            }
            if (!appended) {
                where.eq("1", 0);
            }
            where.endParentheses();
        }

        default boolean appendCondition(WhereBuilder where, String field, Object value) {
            if (value instanceof List) {
                List<?> list = (List<?>) value;
                if (list.isEmpty()) {
                    return false;
                }
                where.in(field, list.toArray());
            } else {
                where.eq(field, value);
            }
            return true;
        }
}
