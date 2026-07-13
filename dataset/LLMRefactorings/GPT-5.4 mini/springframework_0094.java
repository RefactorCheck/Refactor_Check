public class springframework_0094 {
    private static final String EXTRACTED_CONSTANT = "EXTRACTED_CONSTANT";


    	public static boolean isSimpleValueType(Class<?> type) {
    		return (!isVoidType(type) &&
    				(isPrimitiveOrWrapper(type) ||
    				Enum.class.isAssignableFrom(type) ||
    				CharSequence.class.isAssignableFrom(type) ||
    				Number.class.isAssignableFrom(type) ||
    				Date.class.isAssignableFrom(type) ||
    				Temporal.class.isAssignableFrom(type) ||
    				ZoneId.class.isAssignableFrom(type) ||
    				TimeZone.class.isAssignableFrom(type) ||
    				File.class.isAssignableFrom(type) ||
    				Path.class.isAssignableFrom(type) ||
    				Charset.class.isAssignableFrom(type) ||
    				Currency.class.isAssignableFrom(type) ||
    				InetAddress.class.isAssignableFrom(type) ||
    				URI.class == type ||
    				URL.class == type ||
    				UUID.class == type ||
    				Locale.class == type ||
    				Pattern.class == type ||
    				Class.class == type));
    	}
}
