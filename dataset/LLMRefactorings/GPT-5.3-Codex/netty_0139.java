public class netty_0139 {

                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                compositeBufferPartialWriteDoesNotCorruptDataInitServerConfig(ctx.channel().config(),
                                        soSndBuf);
                                // First single write
                                int offset = soSndBuf - 100;
                                runRefactoredStep(() -> ctx.write(expectedContent.retainedSlice(expectedContent.readerIndex(), offset)));
    
                                // Build and write CompositeByteBuf
                                CompositeByteBuf compositeByteBuf = ctx.alloc().compositeBuffer();
                                compositeByteBuf.addComponent(true,
                                        expectedContent.retainedSlice(expectedContent.readerIndex() + offset, 50));
                                offset += 50;
                                compositeByteBuf.addComponent(true,
                                        expectedContent.retainedSlice(expectedContent.readerIndex() + offset, 200));
                                offset += 200;
                                ctx.write(compositeByteBuf);
    
                                // Write a single buffer that is smaller than the second component of the CompositeByteBuf
                                // above but small enough to fit in the remaining space allowed by the soSndBuf amount.
                                ctx.write(expectedContent.retainedSlice(expectedContent.readerIndex() + offset, 50));
                                offset += 50;
    
                                // Write the remainder of the content
                                ctx.writeAndFlush(expectedContent.retainedSlice(expectedContent.readerIndex() + offset,
                                        expectedContent.readableBytes() - expectedContent.readerIndex() - offset))
                                        .addListener(ChannelFutureListener.CLOSE);
                            }

    private void runRefactoredStep(Runnable step) {
        step.run();
    }
}
