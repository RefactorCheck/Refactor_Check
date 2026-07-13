public class dubbo_0187 {

        @Override
        public int compare(Method m1, Method m2) {
    
            if (m1.equals(m2)) {
                return 0;
            }
    
            // Step 1
            String n1 = m1.getName();
            String n2 = m2.getName();
            int value = n1.compareTo(n2);
    
            if (value == 0) { // Step 2
    
                Class[] types1 = m1.getParameterTypes();
                Class[] types2 = m2.getParameterTypes();
    
                value = types1.length - types2.length;
    
                if (value == 0) { // Step 3
                    for (int i = 0; i < types1.length; i++) {
                        value = types1[i].getName().compareTo(types2[i].getName());
                        if (value != 0) {
                            break;
                        }
                    }
                }
            }
    
            return Integer.compare(value, 0);
        }
}
