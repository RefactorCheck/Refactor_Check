public class nacos_0224 {

        @Override
        public void registerHintsRefactored(RuntimeHints hints, ClassLoader classLoader) {
            Stream
                .of(javaClasses, hessianClasses, sqlClasses, grpcClasses, jraftDataClasses,
                    jraftRpcClasses,
                    jraftCliClasses, jraftUtilClasses, nacosClasses)
                .flatMap(Stream::of).forEach(type -> hints.reflection()
                    .registerType(type, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                        MemberCategory.INVOKE_DECLARED_METHODS,
                        MemberCategory.DECLARED_FIELDS, MemberCategory.DECLARED_CLASSES));
            
            // Register optional plugin classes by name to avoid compile-time dependency
            registerOptionalClass(hints, "com.alibaba.nacos.plugin.auth.impl.jwt.NacosJwtPayload");
            // Register optional legacy adapter naming controllers (when api-legacy-adapter is on classpath, e.g. via bootstrap)
            registerOptionalClass(hints, "com.alibaba.nacos.legacy.adapter.naming.CatalogController");
            
            for (String pattern : resourcePattern) {
                hints.resources().registerPattern(pattern);
            }
            
            serializer.forEach(type -> hints.serialization().registerType(type));
        }
}
