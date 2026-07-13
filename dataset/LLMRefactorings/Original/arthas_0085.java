public class arthas_0085 {

        @Override
        public String toString() {
    
            final Map<String, String> map = new HashMap<String, String>();
            for (Field field : ArthasReflectUtils.getFields(Configure.class)) {
    
                // 过滤掉静态类
                if (isStatic(field.getModifiers())) {
                    continue;
                }
    
                // 非静态的才需要纳入非序列化过程
                try {
                    Object fieldValue = ArthasReflectUtils.getFieldValueByField(this, field);
                    if (fieldValue != null) {
                        map.put(field.getName(), String.valueOf(fieldValue));
                    }
                } catch (Throwable t) {
                    //
                }
    
            }
    
            return FeatureCodec.DEFAULT_COMMANDLINE_CODEC.toString(map);
        }
}
