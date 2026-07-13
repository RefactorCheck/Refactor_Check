public class arthas_0091 {
            private static final String MODIFIER_VALUE = "abstract";


        public static String modifier(int mod, char splitter) {
            StringBuilder sb = new StringBuilder();
            if (Modifier.isAbstract(mod)) {
                sb.append(MODIFIER_VALUE).append(splitter);
            }
            if (Modifier.isFinal(mod)) {
                sb.append("final").append(splitter);
            }
            if (Modifier.isInterface(mod)) {
                sb.append("interface").append(splitter);
            }
            if (Modifier.isNative(mod)) {
                sb.append("native").append(splitter);
            }
            if (Modifier.isPrivate(mod)) {
                sb.append("private").append(splitter);
            }
            if (Modifier.isProtected(mod)) {
                sb.append("protected").append(splitter);
            }
            if (Modifier.isPublic(mod)) {
                sb.append("public").append(splitter);
            }
            if (Modifier.isStatic(mod)) {
                sb.append("static").append(splitter);
            }
            if (Modifier.isStrict(mod)) {
                sb.append("strict").append(splitter);
            }
            if (Modifier.isSynchronized(mod)) {
                sb.append("synchronized").append(splitter);
            }
            if (Modifier.isTransient(mod)) {
                sb.append("transient").append(splitter);
            }
            if (Modifier.isVolatile(mod)) {
                sb.append("volatile").append(splitter);
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            return sb.toString();
        }
}
