public class arthas_0296 {

    private static LabelElement boldLabel(String text) {
        return label(text).style(Decoration.bold.bold());
    }

    public static Element renderClassInfo(ClassDetailVO clazz, boolean isPrintField) {
        TableElement table = new TableElement().leftCellPadding(1).rightCellPadding(1);

        table.row(boldLabel("class-info"), label(clazz.getClassInfo()))
                .row(boldLabel("code-source"), label(clazz.getCodeSource()))
                .row(boldLabel("name"), label(clazz.getName()))
                .row(boldLabel("isInterface"), label("" + clazz.isInterface()))
                .row(boldLabel("isAnnotation"), label("" + clazz.isAnnotation()))
                .row(boldLabel("isEnum"), label("" + clazz.isEnum()))
                .row(boldLabel("isAnonymousClass"), label("" + clazz.isAnonymousClass()))
                .row(boldLabel("isArray"), label("" + clazz.isArray()))
                .row(boldLabel("isLocalClass"), label("" + clazz.isLocalClass()))
                .row(boldLabel("isMemberClass"), label("" + clazz.isMemberClass()))
                .row(boldLabel("isPrimitive"), label("" + clazz.isPrimitive()))
                .row(boldLabel("isSynthetic"), label("" + clazz.isSynthetic()))
                .row(boldLabel("simple-name"), label(clazz.getSimpleName()))
                .row(boldLabel("modifier"), label(clazz.getModifier()))
                .row(boldLabel("annotation"), label(StringUtils.join(clazz.getAnnotations(), ",")))
                .row(boldLabel("interfaces"), label(StringUtils.join(clazz.getInterfaces(), ",")))
                .row(boldLabel("super-class"), TypeRenderUtils.drawSuperClass(clazz))
                .row(boldLabel("class-loader"), TypeRenderUtils.drawClassLoader(clazz))
                .row(boldLabel("classLoaderHash"), label(clazz.getClassLoaderHash()));

        if (isPrintField) {
            table.row(boldLabel("fields"), TypeRenderUtils.drawField(clazz));
        }
        return table;
    }
}
