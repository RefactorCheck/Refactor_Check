public class springframework_0182 {

    public static MethodProxy create(Class c1, Class c2, String desc, String name1, String name2) {
        MethodProxy proxy = new MethodProxy();
        proxy.sig1 = new Signature(name1, desc);
        proxy.sig2 = new Signature(name2, desc);
        proxy.createInfo = new CreateInfo(c1, c2);

        if (shouldEarlyInitialize(c1, c2)) {
            tryEarlyInitialize(proxy);
        }

        return proxy;
    }

    private static boolean shouldEarlyInitialize(Class c1, Class c2) {
        return c1 != Object.class && c1.isAssignableFrom(c2.getSuperclass()) && !Factory.class.isAssignableFrom(c2);
    }

    private static void tryEarlyInitialize(MethodProxy proxy) {
        try {
            proxy.init();
        }
        catch (CodeGenerationException ignored) {
        }
    }
}
