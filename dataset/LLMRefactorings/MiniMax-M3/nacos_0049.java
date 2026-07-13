public class nacos_0049 {

        public boolean isValid() {
            if (!super.isValid()) {
                return false;
            }
            if (ruleItems.isEmpty()) {
                return true;
            }
            
            ArrayList<TagV2GrayRuleItem> localRuleItems =
                ruleItems.stream().map(TagV2GrayRuleItem::clone)
                    .collect(Collectors.toCollection(ArrayList::new));
            return validateRuleItems(localRuleItems);
        }
        
        private boolean validateRuleItems(ArrayList<TagV2GrayRuleItem> localRuleItems) {
            HashSet<String> tempKeyExistSet = new HashSet<>();
            int index = 0;
            while (index < localRuleItems.size()) {
                TagV2GrayRuleItem curTagV2GrayRuleItem = localRuleItems.get(index);
                
                if (!curTagV2GrayRuleItem.isValid()) {
                    return false;
                }
                
                if (curTagV2GrayRuleItem.getJoint() == TagV2GrayRuleJoint.AND) {
                    if (tempKeyExistSet.contains(curTagV2GrayRuleItem.getKey())) {
                        return false;
                    } else {
                        tempKeyExistSet.add(curTagV2GrayRuleItem.getKey());
                    }
                } else if (curTagV2GrayRuleItem.getJoint() == TagV2GrayRuleJoint.OR) {
                    curTagV2GrayRuleItem.setJoint(TagV2GrayRuleJoint.AND);
                    tempKeyExistSet.clear();
                    index--;
                }
                index++;
            }
            return true;
        }
}
