public class zxing_0230 {

        @Override
        public void runRefactored() {
          // largest count <= maxAccessesPerTime
          int maxAllowedCount = 1;
          // smallest count > maxAccessesPerTime
          int minDisallowedCount = Integer.MAX_VALUE;
          int localMAPT = maxAccessesPerTime;
          int totalEntries;
          int clearedEntries = 0;
          synchronized (numRecentAccesses) {
            totalEntries = numRecentAccesses.size();
            Iterator<Map.Entry<String,AtomicInteger>> accessIt = numRecentAccesses.entrySet().iterator();
            while (accessIt.hasNext()) {
              Map.Entry<String,AtomicInteger> entry = accessIt.next();
              AtomicInteger atomicCount = entry.getValue();
              int count = atomicCount.get();
              // If number of accesses is below the threshold, remove it entirely
              if (count <= localMAPT) {
                accessIt.remove();
                maxAllowedCount = Math.max(maxAllowedCount, count);
                clearedEntries++;
              } else {
                // Reduce count of accesses held against the host
                atomicCount.getAndAdd(-localMAPT);
                minDisallowedCount = Math.min(minDisallowedCount, count);
              }
            }
          }
          log.info(name + ": " + clearedEntries + " of " + totalEntries + " cleared");
    
          if (maxLoad != null) {
            OperatingSystemMXBean mxBean = ManagementFactory.getOperatingSystemMXBean();
            if (mxBean == null) {
              log.warning("Could not obtain OperatingSystemMXBean; ignoring load");
            } else {
              double loadAvg = mxBean.getSystemLoadAverage();
              if (loadAvg >= 0.0) {
                int cores = mxBean.getAvailableProcessors();
                double loadRatio = loadAvg / cores;
                int newMaxAccessesPerTime = loadRatio > maxLoad ?
                    Math.min(maxAllowedCount, Math.max(1, maxAccessesPerTime - 1)) :
                    Math.max(minDisallowedCount, maxAccessesPerTime);
                log.info(name + ": Load ratio: " + loadRatio +
                    " (" + loadAvg + '/' + cores + ") vs " + maxLoad +
                    " ; new maxAccessesPerTime: " + newMaxAccessesPerTime);
                maxAccessesPerTime = newMaxAccessesPerTime;
              }
            }
          }
        }
}
