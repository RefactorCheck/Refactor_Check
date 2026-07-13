public class dubbo_0116 {

            private CodeBlock generateStatementForElement(
                    ClassName targetClassName, AnnotatedInjectElement referenceElement, RuntimeHints hints) {                return generateStatementForElementExtracted(targetClassName, referenceElement, hints);
}

public class dubbo_0116 {

            private CodeBlock generateStatementForElementExtracted(
                    ClassName targetClassName, AnnotatedInjectElement referenceElement, RuntimeHints hints) {
    
                Member member = referenceElement.getMember();
                AnnotationAttributes attributes = referenceElement.attributes;
                Object injectedObject = referenceElement.injectedObject;
    
                try {
                    Class<?> c = referenceElement.getInjectedType();
                    AotUtils.registerSerializationForService(c, hints);
                    hints.reflection().registerType(TypeReference.of(c), MemberCategory.INVOKE_PUBLIC_METHODS);
                    // need to enumerate all interfaces by the proxy
                    hints.proxies().registerJdkProxy(c, EchoService.class, Destroyable.class);
                    hints.proxies().registerJdkProxy(c, EchoService.class, Destroyable.class, GenericService.class);
                    hints.proxies()
                            .registerJdkProxy(
                                    c,
                                    EchoService.class,
                                    Destroyable.class,
                                    SpringProxy.class,
                                    Advised.class,
                                    DecoratingProxy.class);
                    hints.proxies()
                            .registerJdkProxy(
                                    c,
                                    EchoService.class,
                                    GenericService.class,
                                    Destroyable.class,
                                    SpringProxy.class,
                                    Advised.class,
                                    DecoratingProxy.class);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
    
                if (member instanceof Field) {
                    return generateMethodStatementForField(
                            targetClassName, (Field) member, attributes, injectedObject, hints);
                }
                if (member instanceof Method) {
                    return generateMethodStatementForMethod(
                            targetClassName, (Method) member, attributes, injectedObject, hints);
                }
                throw new IllegalStateException(
                        "Unsupported member type " + member.getClass().getName());
            
}
}
