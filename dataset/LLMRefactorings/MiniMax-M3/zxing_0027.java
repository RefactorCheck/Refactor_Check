public class zxing_0027 {

      private BitMatrix sampleGrid(BitMatrix image,
                                   ResultPoint topLeft,
                                   ResultPoint topRight,
                                   ResultPoint bottomRight,
                                   ResultPoint bottomLeft) throws NotFoundException {
    
        GridSampler sampler = GridSampler.getInstance();
        int dimension = getDimension();
        float center = dimension / 2.0f;
        float low = center - nbCenterLayers;
        float high = center + nbCenterLayers;
    
        return sampler.sampleGrid(image,
                                  dimension,
                                  dimension,
                                  low, low,
                                  high, low,
                                  high, high,
                                  low, high,
                                  topLeft.getX(), topLeft.getY(),
                                  topRight.getX(), topRight.getY(),
                                  bottomRight.getX(), bottomRight.getY(),
                                  bottomLeft.getX(), bottomLeft.getY());
      }
}
