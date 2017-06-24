package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.graph.*;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Value;
import org.neo4j.driver.v1.types.Relationship;
import org.neo4j.driver.v1.util.Pair;

class Neo4jGraphFactory {

    GraphObject create(Record record) {
        GraphResult neo4jResult = new GraphResult();
        for(Pair<String, Value> pair : record.fields()) {
                neo4jResult.add(pair.key(), create(pair.value()));
        }
        return neo4jResult;
    }

    private GraphObject create(Value value) {
        switch (value.type().name()) {
            case "NODE" : return create(value.asNode());
            case "RELATIONSHIP" : return create(value.asRelationship());
            case "BOOLEAN" : return create(value.asRelationship());
            case "INTEGER" : return create(value.asLong());
        }
        return new GraphUnspecifiedType();
    }

    private GraphLong create(long value) {
        return new GraphLong(value);
    }

    private GraphEdge create(Relationship relationship) {
        GraphEdge graphEdge = new GraphEdge();
        graphEdge.setStart(relationship.startNodeId());
        graphEdge.setEnd(relationship.endNodeId());
        graphEdge.setProperties(relationship.asMap());
        graphEdge.setType(relationship.type());
        graphEdge.setId(relationship.id());
        return graphEdge;
    }

    private GraphNode create(org.neo4j.driver.v1.types.Node node) {
        GraphNode neo4JGraphNode = new GraphNode();
        neo4JGraphNode.setProperties(node.asMap());
        for(String label :node.labels()) {
            neo4JGraphNode.addLabel(label);
        }
        neo4JGraphNode.setId(node.id());
        return neo4JGraphNode;
    }
}
