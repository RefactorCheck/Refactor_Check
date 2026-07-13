public class springframework_0296 {

    		@Setup
    		public void setup() {
    			applyExtractedRefactoring();

    			this.input = new int[1024];
    			if (this.accessor.equals("DirectFieldAccessor")) {
    				this.propertyAccessor = new DirectFieldAccessor(this.target);
    			}
    			else {
    				this.propertyAccessor = new BeanWrapperImpl(this.target);
    			}
    			switch (this.customEditor) {
    				case "stringTrimmer" ->
    					this.propertyAccessor.registerCustomEditor(String.class, new StringTrimmerEditor(false));
    				case "numberOnPath" ->
    					this.propertyAccessor.registerCustomEditor(int.class, "array.somePath", new CustomNumberEditor(Integer.class, false));
    				case "numberOnNestedPath" ->
    					this.propertyAccessor.registerCustomEditor(int.class, "array[0].somePath", new CustomNumberEditor(Integer.class, false));
    				case "numberOnType" ->
    					this.propertyAccessor.registerCustomEditor(int.class, new CustomNumberEditor(Integer.class, false));
    			}
    		}

	private void applyExtractedRefactoring() {
    			this.target = new PrimitiveArrayBean();
	}
}
