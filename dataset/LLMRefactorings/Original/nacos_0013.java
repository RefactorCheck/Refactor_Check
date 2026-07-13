public class nacos_0013 {

        public boolean match(Map<String, String> labelsMap) {
            if (ruleItems.isEmpty() || labelsMap == null || labelsMap.isEmpty()) {
                return false;
            }
            ArrayList<TagV2GrayRuleItem> localRuleItems =
                ruleItems.stream().map(TagV2GrayRuleItem::clone)
                    .collect(Collectors.toCollection(ArrayList::new));
            int result = 0;
            int tempResult = 0;
            boolean subRuleMatchFlag = true;
            HashSet<String> tempKeyExistSet = new HashSet<>();
            
            for (int index = 0; index < localRuleItems.size(); index++) {
                if (result > 0) {
                    return true;
                }
                TagV2GrayRuleItem curTagV2GrayRuleItem = localRuleItems.get(index);
                
                if (curTagV2GrayRuleItem.getJoint() == TagV2GrayRuleJoint.AND) {
                    //if AND, will consider the current ruleItem belong to this subRule.
                    
                    //if one of the items in the subRule is not match, will continue to next subRule.
                    if (!subRuleMatchFlag) {
                        continue;
                    }
                    
                    //if the key has already existed in this subRule,
                    // another item with the same key appears which will be considered as a syntax error.
                    if (tempKeyExistSet.contains(curTagV2GrayRuleItem.getKey())) {
                        subRuleMatchFlag = false;
                        continue;
                    } else {
                        tempKeyExistSet.add(curTagV2GrayRuleItem.getKey());
                    }
                    
                    //check current item
                    if (!curTagV2GrayRuleItem.match(labelsMap.get(curTagV2GrayRuleItem.getKey()))) {
                        subRuleMatchFlag = false;
                    }
                    tempResult++;
                } else if (curTagV2GrayRuleItem.getJoint() == TagV2GrayRuleJoint.OR) {
                    //if OR, will consider the current ruleItem belong to the next subRule,
                    // and this subRule contains items between [subRuleBeginIndex, index).
                    
                    //only when subRuleMatchFlag is true, update result.
                    if (subRuleMatchFlag) {
                        result = Math.max(result, tempResult);
                    }
                    curTagV2GrayRuleItem.setJoint(TagV2GrayRuleJoint.AND);
                    subRuleMatchFlag = true;
                    tempKeyExistSet.clear();
                    index--;
                }
            }
            if (subRuleMatchFlag) {
                return Math.max(result, tempResult) > 0;
            }
            return result > 0;
        }
}
