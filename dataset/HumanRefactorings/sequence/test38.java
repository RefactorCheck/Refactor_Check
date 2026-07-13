interface Animal {
    String identifyMyself();
}

interface Mammal extends Animal {
    public default String identifyMyself() {
        return "Mammal";
    }
}
