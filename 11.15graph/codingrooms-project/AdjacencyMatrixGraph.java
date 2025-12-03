import java.util.*;

public class AdjacencyMatrixGraph extends DirectedGraph {
   protected ArrayList<Vertex> vertices = new ArrayList<>();

   // If matrixRows[X][Y] is true, then an edge exists from vertices[X] to
   // vertices[Y]
   protected ArrayList<ArrayList<Boolean>> matrixRows = new ArrayList<>();

   // TODO: Type your additional code here, if desired

   // Creates and adds a new vertex to the graph, provided a vertex with the
   // same label doesn't already exist in the graph. Returns the new vertex on
   // success, null on failure.
   @Override
   public Vertex addVertex(String newVertexLabel) {
      // Check for duplicates
      if (getVertex(newVertexLabel) != null) {
         return null;
      }

      // Create and add the new vertex
      Vertex newVertex = new Vertex(newVertexLabel);
      vertices.add(newVertex);

      // Resize the matrix to accommodate the new vertex
      // 1. Add a new column (false) to every existing row
      for (ArrayList<Boolean> row : matrixRows) {
         row.add(false);
      }

      // 2. Create a new row for the new vertex, filled with false
      ArrayList<Boolean> newRow = new ArrayList<>();
      for (int i = 0; i < vertices.size(); i++) {
         newRow.add(false);
      }
      matrixRows.add(newRow);

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

      // Check if edge already exists
      if (hasEdge(fromVertex, toVertex)) {
         return false;
      }

      // Get indices of the vertices to use as matrix coordinates
      int fromIndex = vertices.indexOf(fromVertex);
      int toIndex = vertices.indexOf(toVertex);

      // Set the intersection in the matrix to true
      matrixRows.get(fromIndex).set(toIndex, true);
      return true;
   }

   // Returns an ArrayList of edges with the specified fromVertex.
   @Override
   public ArrayList<Edge> getEdgesFrom(Vertex fromVertex) {
      ArrayList<Edge> edges = new ArrayList<>();
      
      if (fromVertex == null) {
          return edges;
      }

      int fromIndex = vertices.indexOf(fromVertex);
      
      // Iterate through the row corresponding to 'fromVertex'
      ArrayList<Boolean> row = matrixRows.get(fromIndex);
      for (int i = 0; i < row.size(); i++) {
         // If the value is true, an edge exists to vertices.get(i)
         if (row.get(i)) {
            edges.add(new Edge(fromVertex, vertices.get(i)));
         }
      }

      return edges;
   }

   // Returns an ArrayList of edges with the specified toVertex.
   @Override
   public ArrayList<Edge> getEdgesTo(Vertex toVertex) {
      ArrayList<Edge> edges = new ArrayList<>();
      
      if (toVertex == null) {
          return edges;
      }

      int toIndex = vertices.indexOf(toVertex);

      // Iterate through every row (every potential 'from' vertex)
      for (int i = 0; i < matrixRows.size(); i++) {
         // Check the specific column for our 'toVertex'
         if (matrixRows.get(i).get(toIndex)) {
            edges.add(new Edge(vertices.get(i), toVertex));
         }
      }

      return edges;
   }

   // Returns a vertex with a matching label, or null if no such vertex
   // exists
   @Override
   public Vertex getVertex(String vertexLabel) {
      for (Vertex v : vertices) {
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
      
      int fromIndex = vertices.indexOf(fromVertex);
      int toIndex = vertices.indexOf(toVertex);
      
      // Safety check, though logic should prevent invalid indices if vertices are valid
      if (fromIndex == -1 || toIndex == -1) return false;

      return matrixRows.get(fromIndex).get(toIndex);
   }
}
