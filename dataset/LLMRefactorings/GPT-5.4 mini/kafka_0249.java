public class kafka_0249 {

        @Override
        public int hashCode() {
            final int multiplier = 31;
            int result = 1;
            for (int i = 0; i < this.values.length; i++) {
                BoundField f = this.schema.get(i);
                if (f.def.type.isArray()) {
                    if (this.get(f) != null) {
                        Object[] arrayObject = (Object[]) this.get(f);
                        for (Object arrayItem: arrayObject)
                            result = multiplier * result + arrayItem.hashCode();
                    }
                } else {
                    Object field = this.get(f);
                    if (field != null) {
                        result = multiplier * result + field.hashCode();
                    }
                }
            }
            return result;
        }
}
