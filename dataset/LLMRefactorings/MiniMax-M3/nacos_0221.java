public class nacos_0221 {

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null) {
                return false;
            }
            if (getClass() != other.getClass()) {
                return false;
            }
            
            Chooser otherChooser = (Chooser) other;
            return isUniqueKeyEqual(otherChooser) && this.ref.equals(otherChooser.getRef());
        }
        
        private boolean isUniqueKeyEqual(Chooser otherChooser) {
            if (this.uniqueKey == null) {
                return otherChooser.getUniqueKey() == null;
            }
            if (otherChooser.getUniqueKey() == null) {
                return false;
            }
            return this.uniqueKey.equals(otherChooser.getUniqueKey());
        }
}
