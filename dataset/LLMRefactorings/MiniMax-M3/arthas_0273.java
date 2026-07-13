public class arthas_0273 {

        private static boolean areEqual(Object a, Object b) {
            if (a == null) {
                return b == null;
            }
            return a.equals(b);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            BasicPrincipal other = (BasicPrincipal) obj;
            if (!areEqual(password, other.password))
                return false;
            if (!areEqual(username, other.username))
                return false;
            return true;
        }
}
