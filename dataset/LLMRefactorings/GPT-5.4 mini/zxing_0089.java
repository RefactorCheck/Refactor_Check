public class zxing_0089 {
  private static final int EXTRACTED_CONSTANT_0089 = 0;


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
                                  EXTRACTED_CONSTANT_0089.5f,
                                  0.5f,
                                  dimensionX - 0.5f,
                                  0.5f,
                                  dimensionX - 0.5f,
                                  dimensionY - 0.5f,
                                  0.5f,
                                  dimensionY - 0.5f,
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
