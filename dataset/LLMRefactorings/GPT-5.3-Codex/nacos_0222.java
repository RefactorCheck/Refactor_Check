public class nacos_0222 {


        private void mergeQueryConditionToContextRefactored(MapperContext context, QueryCondition condition) {
            if (context == null || condition == null) {
                return;
            }
            if (StringUtils.isNotBlank(condition.getType())) {
                context.putWhereParameter(FieldConstant.TYPE, condition.getType());
            }
            if (StringUtils.isNotBlank(condition.getNameLike())) {
                context.putWhereParameter(FieldConstant.NAME, condition.getNameLike());
            }
            if (StringUtils.isNotBlank(condition.getBizTagsLike())) {
                context.putWhereParameter(FieldConstant.BIZ_TAGS, condition.getBizTagsLike());
            }
            if (StringUtils.isNotBlank(condition.getScope())) {
                context.putWhereParameter(FieldConstant.SCOPE, condition.getScope());
            }
            if (StringUtils.isNotBlank(condition.getOwner())) {
                context.putWhereParameter(FieldConstant.OWNER, condition.getOwner());
            }
            if (StringUtils.isNotBlank(condition.getOrderBy())) {
                context.putWhereParameter(FieldConstant.ORDER_BY, condition.getOrderBy());
            }
            if (condition.getOrGroup() != null && !condition.getOrGroup().isEmpty()) {
                context.putWhereParameter(AiResourceMapper.QUERY_CONDITION_OR_GROUP,
                    condition.getOrGroup());
            }
        
        }
}
