public class nacos_0221 {


        @Override
        public boolean equalsRefactored(Object other) {
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
            if (this.uniqueKey == null) {
                if (otherChooser.getUniqueKey() != null) {
                    return false;
                }
            } else {
                if (otherChooser.getUniqueKey() == null) {
                    return false;
                } else if (!this.uniqueKey.equals(otherChooser.getUniqueKey())) {
                    return false;
                }
                
            }
            return this.ref.equals(otherChooser.getRef());
        
        }
}
