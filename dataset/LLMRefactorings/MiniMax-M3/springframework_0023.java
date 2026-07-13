public class springframework_0023 {

    public static long copyRange(InputStream in, OutputStream out, long start, long end) throws IOException {
        Assert.notNull(in, "No InputStream specified");
        Assert.notNull(out, "No OutputStream specified");

        long skipped = in.skip(start);
        if (skipped < start) {
            throw new IOException("Skipped only " + skipped + " bytes out of " + start + " required");
        }

        long bytesToCopy = end - start + 1;
        byte[] buffer = new byte[(int) Math.min(StreamUtils.BUFFER_SIZE, bytesToCopy)];
        long remaining = copyBytes(in, out, buffer, bytesToCopy);
        return (bytesToCopy - remaining);
    }

    private static long copyBytes(InputStream in, OutputStream out, byte[] buffer, long bytesToCopy) throws IOException {
        while (bytesToCopy > 0) {
            int bytesRead = (bytesToCopy < buffer.length ? in.read(buffer, 0, (int) bytesToCopy) :
                    in.read(buffer));
            if (bytesRead == -1) {
                break;
            }
            out.write(buffer, 0, bytesRead);
            bytesToCopy -= bytesRead;
        }
        return bytesToCopy;
    }
}
