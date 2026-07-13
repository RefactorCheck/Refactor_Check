public class nacos_0049 {

        public boolean isValid() {
            if (!super.isValid()) {
                return false;
            }
            if (ruleItems.isEmpty()) {
                return true;
            }
            HashSet<String> tempKeyExistSet = new HashSet<>();
            
            ArrayList<TagV2GrayRuleItem> localRuleItems =
                ruleItems.stream().map(TagV2GrayRuleItem::clone)
                    .collect(Collectors.toCollection(ArrayList::new));
            for (int index = 0; index < localRuleItems.size(); index++) {
                TagV2GrayRuleItem curTagV2GrayRuleItem = localRuleItems.get(index);
                
                if (!curTagV2GrayRuleItem.isValid()) {
                    return false;
                }
                
                if (curTagV2GrayRuleItem.getJoint() == TagV2GrayRuleJoint.AND) {
                    //if AND, will consider the current ruleItem belong to this subRule.
                    
                    //if the key has already existed in this subRule,
                    // another item with the same key appears which will be considered as a syntax error.
                    if (tempKeyExistSet.contains(curTagV2GrayRuleItem.getKey())) {
                        return false;
                    } else {
                        tempKeyExistSet.add(curTagV2GrayRuleItem.getKey());
                    }
                } else if (curTagV2GrayRuleItem.getJoint() == TagV2GrayRuleJoint.OR) {
                    //if OR, will consider the current ruleItem belong to the next subRule,
                    // and this subRule contains items between [subRuleBeginIndex, index).
                    
                    //only when subRuleMatchFlag is true, update result.
                    curTagV2GrayRuleItem.setJoint(TagV2GrayRuleJoint.AND);
                    tempKeyExistSet.clear();
                    index--;
                }
            }
            return true;
        }
}
