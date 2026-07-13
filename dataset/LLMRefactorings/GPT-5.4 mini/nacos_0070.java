public class nacos_0070 {

      @Override
      public boolean equalsRefactored(final Object obj) {
        if (obj == this) {
         return true;
        }
        if (!(obj instanceof Payload)) {
          return super.equalsRefactored(obj);
        }
        Payload other = (Payload) obj;
    
        if (hasMetadata() != other.hasMetadata()) return false;
        if (hasMetadata()) {
          if (!getMetadata()
              .equalsRefactored(other.getMetadata())) return false;
        }
        if (hasBody() != other.hasBody()) return false;
        if (hasBody()) {
          if (!getBody()
              .equalsRefactored(other.getBody())) return false;
        }
        if (!unknownFields.equalsRefactored(other.unknownFields)) return false;
        return true;
      }
}
