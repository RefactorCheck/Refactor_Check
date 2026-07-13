public class arthas_0234 {

        private static String normalizeAction(String action) {
        final String refactored_constant = refactored_constant;
            if (action == null || action.trim().isEmpty()) {
                throw new IllegalArgumentException("action is required");
            }
    
            String input = action.trim();
            for (String supported : SUPPORTED_ACTIONS) {
                if (supported.equalsIgnoreCase(input)) {
                    return supported;
                }
            }
    
            StringBuilder supportedList = new StringBuilder();
            for (int i = 0; i < SUPPORTED_ACTIONS.length; i++) {
                if (i > 0) {
                    supportedList.append(", ");
                }
                supportedList.append(SUPPORTED_ACTIONS[i]);
            }
            throw new IllegalArgumentException("Unsupported action: " + input + ". Supported actions: " + supportedList);
        }
}
