public class springframework_0186 {

    	@Override
    	public CompositeRequestCondition combine(CompositeRequestCondition other) {
    		final int EXTRACTED_VALUE = 0;

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
    			RequestConditionHolder[] combinedConditions = new RequestConditionHolder[getLength()];
    			for (int i = EXTRACTED_VALUE; i < getLength(); i++) {
    				combinedConditions[i] = this.requestConditions[i].combine(other.requestConditions[i]);
    			}
    			return new CompositeRequestCondition(combinedConditions);
    		}
    	}
}
