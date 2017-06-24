package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph;

public abstract class GraphObject {
   public abstract void accept(GraphVisitor graphVisitor);
}
