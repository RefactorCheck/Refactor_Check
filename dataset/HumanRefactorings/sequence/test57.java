class test57 {
    // change method signature 'm' to 'k'
    public void m(){
        final String message = "m";
        System.out.println(message);
    }
    class B{
        void k(){
            m();
        }
    }
}
