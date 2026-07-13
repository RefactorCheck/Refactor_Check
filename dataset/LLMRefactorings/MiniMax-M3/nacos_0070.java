public class nacos_0070 {

      @Override
      public boolean equals(final Object obj) {
        if (obj == this) {
         return true;
        }
        if (!(obj instanceof Payload)) {
          return super.equals(obj);
        }
        Payload other = (Payload) obj;
    
        if (!hasSameMetadata(other)) return false;
        if (!hasSameBody(other)) return false;
        if (!unknownFields.equals(other.unknownFields)) return false;
        return true;
      }
      
      private boolean hasSameMetadata(Payload other) {
        if (hasMetadata() != other.hasMetadata()) return false;
        if (hasMetadata()) {
          return getMetadata().equals(other.getMetadata());
        }
        return true;
      }
      
      private boolean hasSameBody(Payload other) {
        if (hasBody() != other.hasBody()) return false;
        if (hasBody()) {
          return getBody().equals(other.getBody());
        }
        return true;
      }
}
