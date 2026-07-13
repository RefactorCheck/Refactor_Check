public class springframework_0277 {

      Symbol addConstant(final Object value) {
        if (value instanceof Integer) {
          return addConstantInteger(((Integer) value).intValue());
        } else if (value instanceof Byte) {
          return addConstantInteger(((Byte) value).intValue());
        } else if (value instanceof Character) {
          return addConstantInteger(((Character) value).charValue());
        } else if (value instanceof Short) {
          return addConstantInteger(((Short) value).intValue());
        } else if (value instanceof Boolean) {
          return addConstantInteger(((Boolean) value).booleanValue() ? 1 : 0);
        } else if (value instanceof Float) {
          return addConstantFloat(((Float) value).floatValue());
        } else if (value instanceof Long) {
          return addConstantLong(((Long) value).longValue());
        } else if (value instanceof Double) {
          return addConstantDouble(((Double) value).doubleValue());
        } else if (value instanceof String) {
          return addConstantString((String) value);
        } else if (value instanceof Type) {

          int typeSort = ((Type) value).getSort();
          if (typeSort == Type.OBJECT) {
            return addConstantClass(((Type) value).getInternalName());
          } else if (typeSort == Type.METHOD) {
            return addConstantMethodType(((Type) value).getDescriptor());
          } else { // ((Type) value) is a primitive or array ((Type) value).
            return addConstantClass(((Type) value).getDescriptor());
          }
        } else if (value instanceof Handle) {
          Handle handle = (Handle) value;
          return addConstantMethodHandle(
              handle.getTag(),
              handle.getOwner(),
              handle.getName(),
              handle.getDesc(),
              handle.isInterface());
        } else if (value instanceof ConstantDynamic) {
          ConstantDynamic constantDynamic = (ConstantDynamic) value;
          return addConstantDynamic(
              constantDynamic.getName(),
              constantDynamic.getDescriptor(),
              constantDynamic.getBootstrapMethod(),
              constantDynamic.getBootstrapMethodArgumentsUnsafe());
        } else {
          throw new IllegalArgumentException("value " + value);
        }
      }
}
