public class nacos_0224 {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            registerReflectionHints(hints);
            registerOptionalClass(hints, "com.alibaba.nacos.plugin.auth.impl.jwt.NacosJwtPayload");
            registerOptionalClass(hints, "com.alibaba.nacos.legacy.adapter.naming.CatalogController");
            for (String pattern : resourcePattern) {
                hints.resources().registerPattern(pattern);
            }
            serializer.forEach(type -> hints.serialization().registerType(type));
        }

        private void registerReflectionHints(RuntimeHints hints) {
            Stream
                .of(javaClasses, hessianClasses, sqlClasses, grpcClasses, jraftDataClasses,
                    jraftRpcClasses,
                    jraftCliClasses, jraftUtilClasses, nacosClasses)
                .flatMap(Stream::of).forEach(type -> hints.reflection()
                    .registerType(type, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                        MemberCategory.INVOKE_DECLARED_METHODS,
                        MemberCategory.DECLARED_FIELDS, MemberCategory.DECLARED_CLASSES));
        }
}
