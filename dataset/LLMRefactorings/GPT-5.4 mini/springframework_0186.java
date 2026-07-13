public class springframework_0186 {
    private RequestConditionHolder[] combinedConditions;


    	@Override
    	public CompositeRequestCondition combine(CompositeRequestCondition other) {
    		if (isEmpty() && other.isEmpty()) {
    			return this;
    		}
    		else if (other.isEmpty()) {
    			return this;
    		}
    		else if (isEmpty()) {
    			return other;
    		}
    		else {
    			assertNumberOfConditions(other);
    			this.combinedConditions = new RequestConditionHolder[getLength()];
    			for (int i = 0; i < getLength(); i++) {
    				combinedConditions[i] = this.requestConditions[i].combine(other.requestConditions[i]);
    			}
    			return new CompositeRequestCondition(combinedConditions);
    		}
    	}
}
