public class nacos_0192<T> {

    private Ref<T> ref;

    public T randomWithWeight() {
        Ref<T> ref = this.ref;
        double random = ThreadLocalRandom.current().nextDouble(0, 1);
        int index = Arrays.binarySearch(ref.weights, random);
        if (index < 0) {
            index = -index - 1;
        } else {
            return ref.items.get(index);
        }

        if (index < ref.weights.length) {
            if (random < ref.weights[index]) {
                return ref.items.get(index);
            }
        }

        if (ref.weights.length == 0) {
            throw new IllegalStateException(
                "Cumulative Weight wrong , the array length is equal to 0.");
        }

        return fallbackItem();
    }

    private T fallbackItem() {
        return ref.items.get(ref.items.size() - 1);
    }
}
