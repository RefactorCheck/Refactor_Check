public class zxing_0027 {

      private BitMatrix sampleGrid(BitMatrix image,
                                   ResultPoint topLeft,
                                   ResultPoint topRight,
                                   ResultPoint bottomRight,
                                   ResultPoint bottomLeft) throws NotFoundException {
    
        GridSampler sampler = GridSampler.getInstance();
        int dimension = getDimension();
    
        float low = dimension / 2.0f - nbCenterLayers;
        float high = dimension / 2.0f + nbCenterLayers;
    
        return sampler.sampleGrid(image,
                                  dimension,
                                  dimension,
                                  low, low,   // topleft
                                  high, low,  // topright
                                  high, high, // bottomright
                                  low, high,  // bottomleft
                                  topLeft.getX(), topLeft.getY(),
                                  topRight.getX(), topRight.getY(),
                                  bottomRight.getX(), bottomRight.getY(),
                                  bottomLeft.getX(), bottomLeft.getY());
      }
}
