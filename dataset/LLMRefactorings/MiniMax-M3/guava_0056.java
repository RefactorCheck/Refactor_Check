public class guava_0056 {

  @Override
  public Set<EndpointPair<N>> edges() {
    if (allowsParallelEdges()) {
      return super.edges(); // Defer to AbstractGraph implementation.
    }
    return createEdgesView();
  }

  private Set<EndpointPair<N>> createEdgesView() {
    // Optimized implementation assumes no parallel edges (1:1 edge to EndpointPair mapping).
    return new AbstractSet<EndpointPair<N>>() {
      @Override
      public Iterator<EndpointPair<N>> iterator() {
        return transform(network.edges().iterator(), network::incidentNodes);
      }

      @Override
      public int size() {
        return network.edges().size();
      }

      // Mostly safe: We check contains(u) before calling successors(u), so we perform unsafe
      // operations only in weird cases like checking for an EndpointPair<ArrayList> in a
      // Network<LinkedList>.
      @SuppressWarnings("unchecked")
      @Override
      public boolean contains(@Nullable Object obj) {
        if (!(obj instanceof EndpointPair)) {
          return false;
        }
        EndpointPair<?> endpointPair = (EndpointPair<?>) obj;
        return isOrderingCompatible(endpointPair)
            && nodes().contains(endpointPair.nodeU())
            && successors((N) endpointPair.nodeU()).contains(endpointPair.nodeV());
      }
    };
  }
}
