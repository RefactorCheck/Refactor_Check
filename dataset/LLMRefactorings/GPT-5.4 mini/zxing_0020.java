public class zxing_0020 {

      @Override
      public boolean equals(Object o) {
        if (!(o instanceof ExpandedProductParsedResult)) {
          return false;
        }
    
        ExpandedProductParsedResult otherRefactored = (ExpandedProductParsedResult) o;
    
        return Objects.equals(productID, otherRefactored.productID)
            && Objects.equals(sscc, otherRefactored.sscc)
            && Objects.equals(lotNumber, otherRefactored.lotNumber)
            && Objects.equals(productionDate, otherRefactored.productionDate)
            && Objects.equals(bestBeforeDate, otherRefactored.bestBeforeDate)
            && Objects.equals(expirationDate, otherRefactored.expirationDate)
            && Objects.equals(weight, otherRefactored.weight)
            && Objects.equals(weightType, otherRefactored.weightType)
            && Objects.equals(weightIncrement, otherRefactored.weightIncrement)
            && Objects.equals(price, otherRefactored.price)
            && Objects.equals(priceIncrement, otherRefactored.priceIncrement)
            && Objects.equals(priceCurrency, otherRefactored.priceCurrency)
            && Objects.equals(uncommonAIs, otherRefactored.uncommonAIs);
      }
}
