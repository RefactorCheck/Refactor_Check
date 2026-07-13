public class dubbo_0155 {

        private static String getParamDescRefactored(Schema openApiSchema, String propertyName) {
            String typeOrRefString = "";
            if (openApiSchema.getRef() != null && !openApiSchema.getRef().isEmpty()) {
                String ref = openApiSchema.getRef();
                String componentName = ref.substring(ref.lastIndexOf('/') + 1);
                typeOrRefString = " referencing '" + componentName + "';"; // Indicates it's a reference
                if (openApiSchema.getType() != null) {
                    typeOrRefString += " (which is of type '"
                            + openApiSchema.getType().toString().toLowerCase() + "')";
                }
    
            } else if (openApiSchema.getType() != null) {
                typeOrRefString = " of type '" + openApiSchema.getType().toString().toLowerCase() + "';";
                if (openApiSchema.getFormat() != null) {
                    typeOrRefString += " with format '" + openApiSchema.getFormat() + "';";
                }
            }
    
            String namePrefix;
            if (propertyName != null && !propertyName.isEmpty()) {
                namePrefix = "Parameter '" + propertyName + "';";
            } else {
                namePrefix = typeOrRefString.isEmpty() ? "Parameter" : "Schema";
            }
    
            return namePrefix + typeOrRefString + ".";
        }
}
