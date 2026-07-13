public class springframework_0198 {

	private static int calculateStringDistance(String s1, String s2) {
		int s1Length = s1.length();
		int s2Length = s2.length();

		if (s1.isEmpty()) {
			return s2Length;
		}
		if (s2.isEmpty()) {
			return s1Length;
		}

		int[][] d = new int[s1Length + 1][s2Length + 1];
		for (int i = 0; i <= s1Length; i++) {
			d[i][0] = i;
		}
		for (int j = 0; j <= s2Length; j++) {
			d[0][j] = j;
		}

		for (int i = 1; i <= s1Length; i++) {
			char c1 = s1.charAt(i - 1);
			for (int j = 1; j <= s2Length; j++) {
				int cost;
				char c2 = s2.charAt(j - 1);
				if (c1 == c2) {
					cost = 0;
				}
				else {
					cost = 1;
				}
				d[i][j] = Math.min(Math.min(d[i - 1][j] + 1, d[i][j - 1] + 1), d[i - 1][j - 1] + cost);
			}
		}

		return d[s1Length][s2Length];
	}
}
