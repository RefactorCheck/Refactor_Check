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
    
        if (hasMetadata() != other.hasMetadata()) return false;
        if (hasMetadata()) {
          if (!getMetadata()
              .equals(other.getMetadata())) return false;
        }
        if (hasBody() != other.hasBody()) return false;
        if (hasBody()) {
          if (!getBody()
              .equals(other.getBody())) return false;
        }
        if (!unknownFields.equals(other.unknownFields)) return false;
        return true;
      }
}
