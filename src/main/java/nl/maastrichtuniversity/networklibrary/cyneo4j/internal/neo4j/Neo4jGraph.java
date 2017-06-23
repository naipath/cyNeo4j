package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j;

import java.util.List;

public class Neo4jGraph<T> {

    private List<T> data;

    Neo4jGraph(List<T> data) {
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }
}
