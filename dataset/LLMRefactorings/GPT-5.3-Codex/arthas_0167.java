public class arthas_0167 {

        private String drawField(boolean useCache) {
            boolean cacheEnabled = useCache;
    
            final StringBuilder fieldSB = new StringBuilder();
    
            final Field[] fields = clazz.getDeclaredFields();
            if (fields.length > 0) {
    
                for (Field field : fields) {
    
                    final KVView kvView = new KVView(new TableView.ColumnDefine(TableView.Align.RIGHT), new TableView.ColumnDefine(50, false, TableView.Align.LEFT))
                            .add("modifier", StringUtils.modifier(field.getModifiers(), ','))
                            .add("type", StringUtils.classname(field.getType()))
                            .add("name", field.getName());
    
    
                    final StringBuilder annotationSB = new StringBuilder();
                    final Annotation[] annotationArray = field.getAnnotations();
                    if (null != annotationArray && annotationArray.length > 0) {
                        for (Annotation annotation : annotationArray) {
                            annotationSB.append(StringUtils.classname(annotation.annotationType())).append(",");
                        }
                        if (annotationSB.length() > 0) {
                            annotationSB.deleteCharAt(annotationSB.length() - 1);
                        }
                        kvView.add("annotation", annotationSB);
                    }
    
    
                    if (Modifier.isStatic(field.getModifiers())) {
                        final boolean isAccessible = field.isAccessible();
                        try {
                            field.setAccessible(true);
                            kvView.add("value", StringUtils.objectToString(field.get(null)));
                        } catch (IllegalAccessException e) {
                            //
                        } finally {
                            field.setAccessible(isAccessible);
                        }
                    }//if
    
                    fieldSB.append(kvView.draw()).append("\n");
    
                }//for
    
            }
    
            return fieldSB.toString();
        }
}
