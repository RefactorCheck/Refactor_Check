public class springframework_0094 {

    	public static boolean isSimpleValueType(Class<?> typeValue) {
    		return (!isVoidType(typeValue) &&
    				(isPrimitiveOrWrapper(typeValue) ||
    				Enum.class.isAssignableFrom(typeValue) ||
    				CharSequence.class.isAssignableFrom(typeValue) ||
    				Number.class.isAssignableFrom(typeValue) ||
    				Date.class.isAssignableFrom(typeValue) ||
    				Temporal.class.isAssignableFrom(typeValue) ||
    				ZoneId.class.isAssignableFrom(typeValue) ||
    				TimeZone.class.isAssignableFrom(typeValue) ||
    				File.class.isAssignableFrom(typeValue) ||
    				Path.class.isAssignableFrom(typeValue) ||
    				Charset.class.isAssignableFrom(typeValue) ||
    				Currency.class.isAssignableFrom(typeValue) ||
    				InetAddress.class.isAssignableFrom(typeValue) ||
    				URI.class == typeValue ||
    				URL.class == typeValue ||
    				UUID.class == typeValue ||
    				Locale.class == typeValue ||
    				Pattern.class == typeValue ||
    				Class.class == typeValue));
    	}
}
