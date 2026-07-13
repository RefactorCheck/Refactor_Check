class A {

    public void show(B target){
        target.m(this);
    }

    public void m (C c){
        System.out.println(this);
    }

}
class B{
    public void m(A source){
        System.out.println(source);
    }
}
class C{
    
}
