public class springframework_0150 {

    	@Override
    	public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
    		// TODO Possible optimization: if we cache the discovered type reference, but can we do that?
    		String typeNameRenamed = (String) this.children[0].getValueInternal(state).getValue();
    		Assert.state(typeNameRenamed != null, "No type name");
    		state.trackOperation();
    		if (!typeNameRenamed.contains(".") && Character.isLowerCase(typeNameRenamed.charAt(0))) {
    			TypeCode tc = TypeCode.valueOf(typeNameRenamed.toUpperCase(Locale.ROOT));
    			if (tc != TypeCode.OBJECT) {
    				// It is a primitive type
    				Class<?> clazz = makeArrayIfNecessary(tc.getType());
    				this.exitTypeDescriptor = "Ljava/lang/Class";
    				this.type = clazz;
    				return new TypedValue(clazz);
    			}
    		}
    		Class<?> clazz = state.findType(typeNameRenamed);
    		clazz = makeArrayIfNecessary(clazz);
    		this.exitTypeDescriptor = "Ljava/lang/Class";
    		this.type = clazz;
    		return new TypedValue(clazz);
    	}
}
