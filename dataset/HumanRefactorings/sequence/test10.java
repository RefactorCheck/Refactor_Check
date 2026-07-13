public class test10 {
    public int fred(){
        return super.hashCode();
    }
    void f(int ignoredParameter){
        new Ad(){
            void f(){
                int result= Ad.this.fred();
            }
        };
    }
}
