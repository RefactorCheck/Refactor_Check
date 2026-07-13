public class nacos_0079 {

        @Override
        protected void parse(String rawRule) throws NacosException {
            this.isPatternMatch(rawRule, EXPRESSION_PATTERN);
            String[] splitSubExpressionByOrArray = rawRule.trim()
                .split(MultiTagMatchGrayRule.TagV2GrayRuleJoint.OR_REGEXP.getExpression());
            for (String s : splitSubExpressionByOrArray) {
                if (StringUtils.isBlank(s)) {
                    continue;
                }
                String[] splitSubExpressionByAndArray = s.trim()
                    .split(MultiTagMatchGrayRule.TagV2GrayRuleJoint.AND_REGEXP.getExpression());
                for (int andIndex = 0; andIndex < splitSubExpressionByAndArray.length; andIndex++) {
                    if (StringUtils.isBlank(splitSubExpressionByAndArray[andIndex])) {
                        continue;
                    }
                    MultiTagMatchGrayRule.TagV2GrayRuleItem tagV2GrayRuleItem =
                        parseAndExpression(splitSubExpressionByAndArray[andIndex]);
                    if (andIndex == 0) {
                        tagV2GrayRuleItem.setJoint(MultiTagMatchGrayRule.TagV2GrayRuleJoint.OR);
                    }
                    if (this.ruleItems == null) {
                        this.ruleItems = new ArrayList<>();
                    }
                    ruleItems.add(tagV2GrayRuleItem);
                }
            }
        }

        private MultiTagMatchGrayRule.TagV2GrayRuleItem parseAndExpression(String andExpression) throws NacosException {
            String[] keyValueArray = andExpression.trim().split(EQUAL_PATTERN);
            if (keyValueArray.length != 2) {
                throw new NacosException(NacosException.INVALID_PARAM, String.format(
                    "tagv2 gray rule parse failed: key and value's[%s] doesn't match pattern[%s].",
                    andExpression,
                    KEY_PATTERN + EQUAL_PATTERN + VALUE_PATTERN));
            }
            isPatternMatch(keyValueArray[0].trim(), KEY_PATTERN);
            isPatternMatch(keyValueArray[1].trim(), VALUE_PATTERN);
            Set<String> values = Arrays.stream(keyValueArray[1].split(VALUE_SPLITER_PATTERN))
                .map(String::trim)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
            return new MultiTagMatchGrayRule.TagV2GrayRuleItem(keyValueArray[0].trim(), values);
        }
}
