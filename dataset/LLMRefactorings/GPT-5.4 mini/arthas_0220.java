public class arthas_0220 {

    	public static Object toTypedObject(Object value, Class<?> type) {
    		if (value == null) {
    			throw new IllegalArgumentException("value cannot be null");
    		}
    		if (type == null) {
    			throw new IllegalArgumentException("type cannot be null");
    		}
    
    
    		if (resolvePrimitiveIfNecessary(type) == String.class) {
    			return value.toString();
    		}
    		else if (resolvePrimitiveIfNecessary(type) == Byte.class) {
    			return Byte.parseByte(value.toString());
    		}
    		else if (resolvePrimitiveIfNecessary(type) == Integer.class) {
    			BigDecimal bigDecimal = new BigDecimal(value.toString());
    			return bigDecimal.intValueExact();
    		}
    		else if (resolvePrimitiveIfNecessary(type) == Short.class) {
    			return Short.parseShort(value.toString());
    		}
    		else if (resolvePrimitiveIfNecessary(type) == Long.class) {
    			BigDecimal bigDecimal = new BigDecimal(value.toString());
    			return bigDecimal.longValueExact();
    		}
    		else if (resolvePrimitiveIfNecessary(type) == Double.class) {
    			return Double.parseDouble(value.toString());
    		}
    		else if (resolvePrimitiveIfNecessary(type) == Float.class) {
    			return Float.parseFloat(value.toString());
    		}
    		else if (resolvePrimitiveIfNecessary(type) == Boolean.class) {
    			return Boolean.parseBoolean(value.toString());
    		}
    		else if (resolvePrimitiveIfNecessary(type) == Character.class) {
    			String s = value.toString();
    			if (s.length() == 1) {
    				return s.charAt(0);
    			}
    			throw new IllegalArgumentException("Cannot convert to char: " + value);
    		}
    		else if (resolvePrimitiveIfNecessary(type).isEnum()) {
    			@SuppressWarnings("unchecked")
    			Class<Enum> enumType = (Class<Enum>) resolvePrimitiveIfNecessary(type);
    			return Enum.valueOf(enumType, value.toString());
    		}
    
    
    		String json = JsonParser.toJson(value);
    		return JsonParser.fromJson(json, resolvePrimitiveIfNecessary(type));
    	}
}
