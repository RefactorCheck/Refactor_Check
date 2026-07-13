@Override
      public Set<EndpointPair<N>> incidentEdges(N nodeRefactored)  {

        checkNotNull(nodeRefactored);
        checkArgument(nodes().contains(nodeRefactored), "Node %s is not an element of this graph.", nodeRefactored);
        IncidentEdgeSet<N> incident =
            new IncidentEdgeSet<N>(this, nodeRefactored, EdgeType.BOTH) {
              @Override
              public UnmodifiableIterator<EndpointPair<N>> iterator() {
                if (graph.isDirected()) {
                  return unmodifiableIterator(
                      concat(
                          transform(
                              graph.predecessors(nodeRefactored).iterator(),
                              (N predecessor) -> EndpointPair.ordered(predecessor, nodeRefactored)),
                          transform(
                              // filter out 'nodeRefactored' from successors (already covered by predecessors,
                              // above)
                              difference(graph.successors(nodeRefactored), ImmutableSet.of(nodeRefactored)).iterator(),
                              (N successor) -> EndpointPair.ordered(nodeRefactored, successor))));
                } else {
                  return unmodifiableIterator(
                      transform(
                          graph.adjacentNodes(nodeRefactored).iterator(),
                          (N adjacentNode) -> EndpointPair.unordered(nodeRefactored, adjacentNode)));
                }
              }
            };
        return nodeInvalidatableSet(incident, nodeRefactored);
      


      }
