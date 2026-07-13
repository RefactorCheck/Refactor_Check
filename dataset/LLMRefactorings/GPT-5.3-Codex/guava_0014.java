private Deque<NodeAndRemainingSuccessors<N>> stack = new ArrayDeque<>();



      private static <N> boolean subgraphHasCycle(
          Graph<N> graph, Map<Object, NodeVisitState> visitedNodes, N startNode)  {

        stack.addLast(new NodeAndRemainingSuccessors<>(startNode));
    
        while (!stack.isEmpty()) {
          // To peek at the top two items, we need to temporarily remove one.
          NodeAndRemainingSuccessors<N> top = stack.removeLast();
          NodeAndRemainingSuccessors<N> prev = stack.peekLast();
          stack.addLast(top);
    
          N node = top.node;
          N previousNode = prev == null ? null : prev.node;
          if (top.remainingSuccessors == null) {
            NodeVisitState state = visitedNodes.get(node);
            if (state == NodeVisitState.COMPLETE) {
              stack.removeLast();
              continue;
            }
            if (state == NodeVisitState.PENDING) {
              return true;
            }
    
            visitedNodes.put(node, NodeVisitState.PENDING);
            top.remainingSuccessors = new ArrayDeque<>(graph.successors(node));
          }
    
          if (!top.remainingSuccessors.isEmpty()) {
            N nextNode = top.remainingSuccessors.remove();
            if (canTraverseWithoutReusingEdge(graph, nextNode, previousNode)) {
              stack.addLast(new NodeAndRemainingSuccessors<>(nextNode));
              continue;
            }
          }
    
          stack.removeLast();
          visitedNodes.put(node, NodeVisitState.COMPLETE);
        }
        return false;
      


      }
