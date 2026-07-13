class A {
    void m(){
        final int i= 0;
        class LocalB{
            //rename j to i;
            int j;
            void k(){
                j= i;
            }
        }
    };
}
