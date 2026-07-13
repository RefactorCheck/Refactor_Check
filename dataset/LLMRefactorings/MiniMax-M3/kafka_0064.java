public class kafka_0064 {

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Struct other = (Struct) obj;
            if (schema != other.schema)
                return false;
            for (int i = 0; i < this.values.length; i++) {
                BoundField f = this.schema.get(i);
                if (!fieldEquals(f, other))
                    return false;
            }
            return true;
        }

        private boolean fieldEquals(BoundField f, Struct other) {
            if (f.def.type.isArray()) {
                return Arrays.equals((Object[]) this.get(f), (Object[]) other.get(f));
            }
            return Objects.equals(this.get(f), other.get(f));
        }
}
