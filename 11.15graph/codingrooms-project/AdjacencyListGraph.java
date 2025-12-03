import java.util.*;

public class AdjacencyListGraph extends DirectedGraph {
   protected ArrayList<AdjacencyListVertex> vertices = new ArrayList<>();

   // Creates and adds a new vertex to the graph, provided a vertex with the
   // same label doesn't already exist in the graph. Returns the new vertex on
   // success, null on failure.
   @Override
   public Vertex addVertex(String newVertexLabel) {
      // Check if the vertex already exists to avoid duplicates
      if (getVertex(newVertexLabel) != null) {
         return null;
      }
      
      // Create the specific type of vertex required for this graph implementation
      AdjacencyListVertex newVertex = new AdjacencyListVertex(newVertexLabel);
      vertices.add(newVertex);
      return newVertex;
   }

   // Adds a directed edge from the first to the second vertex. If the edge
   // already exists in the graph, no change is made and false is returned.
   // Otherwise the new edge is added and true is returned.
   @Override
   public boolean addDirectedEdge(Vertex fromVertex, Vertex toVertex) {
      // Validate inputs
      if (fromVertex == null || toVertex == null) {
         return false;
      }
      
      // Check if edge already exists using our helper method
      if (hasEdge(fromVertex, toVertex)) {
         return false;
      }

      // We must cast the generic Vertex to AdjacencyListVertex to access its internal 'adjacent' list
      AdjacencyListVertex from = (AdjacencyListVertex) fromVertex;
      
      // Add the 'to' vertex to the 'from' vertex's adjacency list
      from.adjacent.add(toVertex);
      return true;
   }

   // Returns an ArrayList of edges with the specified fromVertex.
   @Override
   public ArrayList<Edge> getEdgesFrom(Vertex fromVertex) {
      ArrayList<Edge> edges = new ArrayList<>();
      
      // Validate input
      if (fromVertex == null) {
         return edges;
      }

      // Cast to access the adjacency list
      AdjacencyListVertex from = (AdjacencyListVertex) fromVertex;
      
      // Convert each adjacent vertex into an Edge object
      for (Vertex to : from.adjacent) {
         edges.add(new Edge(from, to));
      }
      
      return edges;
   }

   // Returns an ArrayList of edges with the specified toVertex.
   @Override
   public ArrayList<Edge> getEdgesTo(Vertex toVertex) {
      ArrayList<Edge> edges = new ArrayList<>();
      
      // Validate input
      if (toVertex == null) {
         return edges;
      }

      // In an adjacency list, finding "edges to" is slow (O(V + E)).
      // We must iterate through EVERY vertex in the graph to see if it points to our target.
      for (AdjacencyListVertex from : vertices) {
         // Check if this vertex has an edge pointing to 'toVertex'
         if (from.adjacent.contains(toVertex)) {
            edges.add(new Edge(from, toVertex));
         }
      }
      
      return edges;
   }

   // Returns a vertex with a matching label, or null if no such vertex
   // exists
   @Override
   public Vertex getVertex(String vertexLabel) {
      // Linear search through the list of vertices
      for (AdjacencyListVertex v : vertices) {
         if (v.getLabel().equals(vertexLabel)) {
            return v;
         }
      }
      return null;
   }

   // Returns true if this graph has an edge from fromVertex to toVertex
   @Override
   public boolean hasEdge(Vertex fromVertex, Vertex toVertex) {
      if (fromVertex == null || toVertex == null) {
         return false;
      }
      
      // Cast so we can look at the adjacency list
      AdjacencyListVertex from = (AdjacencyListVertex) fromVertex;
      
      // The 'contains' method uses .equals(), which checks object reference
      return from.adjacent.contains(toVertex);
   }
}
