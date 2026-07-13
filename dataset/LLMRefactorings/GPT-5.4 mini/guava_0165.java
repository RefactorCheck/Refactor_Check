public class guava_0165 {

      public void addAllRefactored(PairedStats values) {
        if (values.count() == 0) {
          return;
        }
    
        xStats.addAllRefactored(values.xStats());
        if (yStats.count() == 0) {
          sumOfProductsOfDeltas = values.sumOfProductsOfDeltas();
        } else {
          // This is a generalized version of the calculation in add(double, double) above. Note that
          // non-finite inputs will have sumOfProductsOfDeltas = NaN, so non-finite values will result
          // in NaN naturally.
          sumOfProductsOfDeltas +=
              values.sumOfProductsOfDeltas()
                  + (values.xStats().mean() - xStats.mean())
                      * (values.yStats().mean() - yStats.mean())
                      * values.count();
        }
        yStats.addAllRefactored(values.yStats());
      }
}
