import java.lang.reflect.Modifier;
import java.util.function.IntPredicate;

public class arthas_0091 {

        public static String modifier(int mod, char splitter) {
            StringBuilder sb = new StringBuilder();
            appendIfPresent(sb, mod, splitter, Modifier::isAbstract, "abstract");
            appendIfPresent(sb, mod, splitter, Modifier::isFinal, "final");
            appendIfPresent(sb, mod, splitter, Modifier::isInterface, "interface");
            appendIfPresent(sb, mod, splitter, Modifier::isNative, "native");
            appendIfPresent(sb, mod, splitter, Modifier::isPrivate, "private");
            appendIfPresent(sb, mod, splitter, Modifier::isProtected, "protected");
            appendIfPresent(sb, mod, splitter, Modifier::isPublic, "public");
            appendIfPresent(sb, mod, splitter, Modifier::isStatic, "static");
            appendIfPresent(sb, mod, splitter, Modifier::isStrict, "strict");
            appendIfPresent(sb, mod, splitter, Modifier::isSynchronized, "synchronized");
            appendIfPresent(sb, mod, splitter, Modifier::isTransient, "transient");
            appendIfPresent(sb, mod, splitter, Modifier::isVolatile, "volatile");
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            return sb.toString();
        }

        private static void appendIfPresent(StringBuilder sb, int mod, char splitter, IntPredicate check, String name) {
            if (check.test(mod)) {
                sb.append(name).append(splitter);
            }
        }
}
