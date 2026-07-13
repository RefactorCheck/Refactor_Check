public class zxing_0089 {

      private static final float CENTER_OFFSET = 0.5f;

      private static BitMatrix sampleGrid(BitMatrix image,
                                          ResultPoint topLeft,
                                          ResultPoint bottomLeft,
                                          ResultPoint bottomRight,
                                          ResultPoint topRight,
                                          int dimensionX,
                                          int dimensionY) throws NotFoundException {
    
        GridSampler sampler = GridSampler.getInstance();
    
        return sampler.sampleGrid(image,
                                  dimensionX,
                                  dimensionY,
                                  CENTER_OFFSET,
                                  CENTER_OFFSET,
                                  dimensionX - CENTER_OFFSET,
                                  CENTER_OFFSET,
                                  dimensionX - CENTER_OFFSET,
                                  dimensionY - CENTER_OFFSET,
                                  CENTER_OFFSET,
                                  dimensionY - CENTER_OFFSET,
                                  topLeft.getX(),
                                  topLeft.getY(),
                                  topRight.getX(),
                                  topRight.getY(),
                                  bottomRight.getX(),
                                  bottomRight.getY(),
                                  bottomLeft.getX(),
                                  bottomLeft.getY());
      }
}
