public class arthas_0220 {

    	public static Object toTypedObjectRefactored(Object value, Class<?> type) {
    		if (value == null) {
    			throw new IllegalArgumentException("value cannot be null");
    		}
    		if (type == null) {
    			throw new IllegalArgumentException("type cannot be null");
    		}
    
    		Class<?> javaType = resolvePrimitiveIfNecessary(type);
    
    		if (javaType == String.class) {
    			return value.toString();
    		}
    		else if (javaType == Byte.class) {
    			return Byte.parseByte(value.toString());
    		}
    		else if (javaType == Integer.class) {
    			BigDecimal bigDecimal = new BigDecimal(value.toString());
    			return bigDecimal.intValueExact();
    		}
    		else if (javaType == Short.class) {
    			return Short.parseShort(value.toString());
    		}
    		else if (javaType == Long.class) {
    			BigDecimal bigDecimal = new BigDecimal(value.toString());
    			return bigDecimal.longValueExact();
    		}
    		else if (javaType == Double.class) {
    			return Double.parseDouble(value.toString());
    		}
    		else if (javaType == Float.class) {
    			return Float.parseFloat(value.toString());
    		}
    		else if (javaType == Boolean.class) {
    			return Boolean.parseBoolean(value.toString());
    		}
    		else if (javaType == Character.class) {
    			String s = value.toString();
    			if (s.length() == 1) {
    				return s.charAt(0);
    			}
    			throw new IllegalArgumentException("Cannot convert to char: " + value);
    		}
    		else if (javaType.isEnum()) {
    			@SuppressWarnings("unchecked")
    			Class<Enum> enumType = (Class<Enum>) javaType;
    			return Enum.valueOf(enumType, value.toString());
    		}
    
    
    		String json = JsonParser.toJson(value);
    		return JsonParser.fromJson(json, javaType);
    	}
}
