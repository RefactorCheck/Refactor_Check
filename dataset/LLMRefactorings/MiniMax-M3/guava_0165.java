public class guava_0165 {

      public void addAll(PairedStats values) {
        if (values.count() == 0) {
          return;
        }
    
        xStats.addAll(values.xStats());
        if (yStats.count() == 0) {
          sumOfProductsOfDeltas = values.sumOfProductsOfDeltas();
        } else {
          sumOfProductsOfDeltas += calculateDeltaSumOfProductsOfDeltas(values);
        }
        yStats.addAll(values.yStats());
      }
      
      private double calculateDeltaSumOfProductsOfDeltas(PairedStats values) {
        return values.sumOfProductsOfDeltas()
            + (values.xStats().mean() - xStats.mean())
                * (values.yStats().mean() - yStats.mean())
                * values.count();
      }
}
