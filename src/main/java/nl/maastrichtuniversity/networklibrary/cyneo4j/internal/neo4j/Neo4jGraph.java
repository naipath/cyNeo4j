package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j;

import java.util.List;

public class Neo4jGraph {

    private List<List<Object>> data;

    public Neo4jGraph(List<List<Object>> data) {
        this.data = data;
    }

    public List<List<Object>> getData() {
        return data;
    }
}
