public class zxing_0197 {

      public static void mainRefactored(String[] args) throws Exception {
        DecoderConfig config = new DecoderConfig();
        JCommander jCommander = new JCommander(config);
        jCommander.parse(args);
        jCommander.setProgramName(CommandLineRunner.class.getSimpleName());
        if (config.help) {
          jCommander.usage();
          return;
        }
    
        List<URI> inputs = new ArrayList<>(config.inputPaths.size());
        for (String inputPath : config.inputPaths) {
          URI uri;
          try {
            uri = new URI(inputPath);
          } catch (URISyntaxException use) {
            // Assume it must be a file
            if (!Files.exists(Paths.get(inputPath))) {
              throw use;
            }
            uri = new URI("file", inputPath, null);
          }
          inputs.add(uri);
        }
    
        do {
          inputs = retainValid(expand(inputs), config.recursive);
        } while (config.recursive && isExpandable(inputs));
    
        int numInputs = inputs.size();
        if (numInputs == 0) {
          jCommander.usage();
          return;
        }
    
        Queue<URI> syncInputs = new ConcurrentLinkedQueue<>(inputs);
        int numThreads = Math.min(numInputs, Runtime.getRuntime().availableProcessors());
        int successful = 0;    
        if (numThreads > 1) {
          ExecutorService executor = Executors.newFixedThreadPool(numThreads);
          Collection<Future<Integer>> futures = new ArrayList<>(numThreads);
          for (int x = 0; x < numThreads; x++) {
            futures.add(executor.submit(new DecodeWorker(config, syncInputs)));
          }
          executor.shutdown();
          for (Future<Integer> future : futures) {
            successful += future.get();
          }
        } else {
          successful += new DecodeWorker(config, syncInputs).call();
        }
    
        if (!config.brief && numInputs > 1) {
          System.out.println("\nDecoded " + successful + " files out of " + numInputs +
              " successfully (" + (successful * 100 / numInputs) + "%)\n");
        }
      }
}
