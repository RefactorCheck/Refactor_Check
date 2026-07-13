public class kafka_0249 {

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            for (int i = 0; i < this.values.length; i++) {
                BoundField f = this.schema.get(i);
                if (f.def.type.isArray()) {
                    if (this.get(f) != null) {
                        Object[] arrayObject = (Object[]) this.get(f);
                        for (Object arrayItem: arrayObject)
                            result = prime * result + arrayItem.hashCode();
                    }
                } else {
                    Object field = this.get(f);
                    if (field != null) {
                        result = prime * result + field.hashCode();
                    }
                }
            }
            return result;
        }
}
