public class springframework_0198 {

    	private static int calculateStringDistance(String s1, String s2) {
    		if (s1.isEmpty()) {
    			return s2.length();
    		}
    		if (s2.isEmpty()) {
    			return s1.length();
    		}
    

    		for (int i = 0; i <= s1.length(); i++) {
    			(new int[s1.length() + 1][s2.length() + 1])[i][0] = i;
    		}
    		for (int j = 0; j <= s2.length(); j++) {
    			(new int[s1.length() + 1][s2.length() + 1])[0][j] = j;
    		}
    
    		for (int i = 1; i <= s1.length(); i++) {
    			char c1 = s1.charAt(i - 1);
    			for (int j = 1; j <= s2.length(); j++) {
    				int cost;
    				char c2 = s2.charAt(j - 1);
    				if (c1 == c2) {
    					cost = 0;
    				}
    				else {
    					cost = 1;
    				}
    				(new int[s1.length() + 1][s2.length() + 1])[i][j] = Math.min(Math.min((new int[s1.length() + 1][s2.length() + 1])[i - 1][j] + 1, (new int[s1.length() + 1][s2.length() + 1])[i][j - 1] + 1), (new int[s1.length() + 1][s2.length() + 1])[i - 1][j - 1] + cost);
    			}
    		}
    
    		return (new int[s1.length() + 1][s2.length() + 1])[s1.length()][s2.length()];
    	}
}
