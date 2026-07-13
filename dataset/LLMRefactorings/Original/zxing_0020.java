public class zxing_0020 {

      @Override
      public boolean equals(Object o) {
        if (!(o instanceof ExpandedProductParsedResult)) {
          return false;
        }
    
        ExpandedProductParsedResult other = (ExpandedProductParsedResult) o;
    
        return Objects.equals(productID, other.productID)
            && Objects.equals(sscc, other.sscc)
            && Objects.equals(lotNumber, other.lotNumber)
            && Objects.equals(productionDate, other.productionDate)
            && Objects.equals(bestBeforeDate, other.bestBeforeDate)
            && Objects.equals(expirationDate, other.expirationDate)
            && Objects.equals(weight, other.weight)
            && Objects.equals(weightType, other.weightType)
            && Objects.equals(weightIncrement, other.weightIncrement)
            && Objects.equals(price, other.price)
            && Objects.equals(priceIncrement, other.priceIncrement)
            && Objects.equals(priceCurrency, other.priceCurrency)
            && Objects.equals(uncommonAIs, other.uncommonAIs);
      }
}
