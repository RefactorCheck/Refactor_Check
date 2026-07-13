public class springframework_0150 {

	@Override
	public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
		// TODO Possible optimization: if we cache the discovered type reference, but can we do that?
		String typeName = (String) this.children[0].getValueInternal(state).getValue();
		Assert.state(typeName != null, "No type name");
		state.trackOperation();
		if (!typeName.contains(".") && Character.isLowerCase(typeName.charAt(0))) {
			TypeCode tc = TypeCode.valueOf(typeName.toUpperCase(Locale.ROOT));
			if (tc != TypeCode.OBJECT) {
				// It is a primitive type
				return createClassTypedValue(makeArrayIfNecessary(tc.getType()));
			}
		}
		Class<?> clazz = state.findType(typeName);
		return createClassTypedValue(makeArrayIfNecessary(clazz));
	}

	private TypedValue createClassTypedValue(Class<?> clazz) {
		this.exitTypeDescriptor = "Ljava/lang/Class";
		this.type = clazz;
		return new TypedValue(clazz);
	}
}
