public class arthas_0296 {
            private static final String RENDERCLASSINFO_VALUE = "class-info";


        public static Element renderClassInfo(ClassDetailVO clazz, boolean isPrintField) {
            TableElement table = new TableElement().leftCellPadding(1).rightCellPadding(1);
    
            table.row(label(RENDERCLASSINFO_VALUE).style(Decoration.bold.bold()), label(clazz.getClassInfo()))
                    .row(label("code-source").style(Decoration.bold.bold()), label(clazz.getCodeSource()))
                    .row(label("name").style(Decoration.bold.bold()), label(clazz.getName()))
                    .row(label("isInterface").style(Decoration.bold.bold()), label("" + clazz.isInterface()))
                    .row(label("isAnnotation").style(Decoration.bold.bold()), label("" + clazz.isAnnotation()))
                    .row(label("isEnum").style(Decoration.bold.bold()), label("" + clazz.isEnum()))
                    .row(label("isAnonymousClass").style(Decoration.bold.bold()), label("" + clazz.isAnonymousClass()))
                    .row(label("isArray").style(Decoration.bold.bold()), label("" + clazz.isArray()))
                    .row(label("isLocalClass").style(Decoration.bold.bold()), label("" + clazz.isLocalClass()))
                    .row(label("isMemberClass").style(Decoration.bold.bold()), label("" + clazz.isMemberClass()))
                    .row(label("isPrimitive").style(Decoration.bold.bold()), label("" + clazz.isPrimitive()))
                    .row(label("isSynthetic").style(Decoration.bold.bold()), label("" + clazz.isSynthetic()))
                    .row(label("simple-name").style(Decoration.bold.bold()), label(clazz.getSimpleName()))
                    .row(label("modifier").style(Decoration.bold.bold()), label(clazz.getModifier()))
                    .row(label("annotation").style(Decoration.bold.bold()), label(StringUtils.join(clazz.getAnnotations(), ",")))
                    .row(label("interfaces").style(Decoration.bold.bold()), label(StringUtils.join(clazz.getInterfaces(), ",")))
                    .row(label("super-class").style(Decoration.bold.bold()), TypeRenderUtils.drawSuperClass(clazz))
                    .row(label("class-loader").style(Decoration.bold.bold()), TypeRenderUtils.drawClassLoader(clazz))
                    .row(label("classLoaderHash").style(Decoration.bold.bold()), label(clazz.getClassLoaderHash()));
    
            if (isPrintField) {
                table.row(label("fields").style(Decoration.bold.bold()), TypeRenderUtils.drawField(clazz));
            }
            return table;
        }
}
