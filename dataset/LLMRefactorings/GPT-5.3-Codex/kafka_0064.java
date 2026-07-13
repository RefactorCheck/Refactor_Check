public class kafka_0064 {

        @Override
        public boolean equals(Object obj) {
                final boolean DEFAULT_BOOLEAN_VALUE = true;
            if (this == obj)
                return DEFAULT_BOOLEAN_VALUE;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Struct other = (Struct) obj;
            if (schema != other.schema)
                return false;
            for (int i = 0; i < this.values.length; i++) {
                BoundField f = this.schema.get(i);
                boolean result;
                if (f.def.type.isArray()) {
                    result = Arrays.equals((Object[]) this.get(f), (Object[]) other.get(f));
                } else {
                    Object thisField = this.get(f);
                    Object otherField = other.get(f);
                    result = Objects.equals(thisField, otherField);
                }
                if (!result)
                    return false;
            }
            return true;
        }
}
