public class test24 {
    public boolean isConsideredFinal(int currentCl) {
        int expectedCl = cl();
        return currentCl == expectedCl
                && countSeenDirectlyAtCl() == countExpectedDirectlyAtCl();
    }

}
