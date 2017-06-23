package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.ResType;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.ResultObject;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.exceptions.AuthenticationException;
import org.neo4j.driver.v1.exceptions.ServiceUnavailableException;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Relationship;

import java.util.List;
import java.util.function.Function;

public class Neo4jClient {

    private Driver driver;

    public boolean connect(ConnectionParameter connectionParameter) {
        try {
            driver = GraphDatabase.driver(
                connectionParameter.getBoltUrl(),
                AuthTokens.basic(
                    connectionParameter.getUsername(),
                    connectionParameter.getPasswordAsString()
                ),
                Config.build().withoutEncryption().toConfig()
            );
            return true;
        } catch (AuthenticationException | ServiceUnavailableException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Neo4jGraph<Long> executeQueryIdList(CypherQuery cypherQuery) {
        return execute(cypherQuery, this::firstResultAsLong);
    }

    public Neo4jGraph<ResultObject> executeQueryResultObject(CypherQuery cypherQuery) {
        return execute(cypherQuery, this::toResultObject);
    }

    private ResultObject toResultObject(Record record) {

        if(record.get(0).asEntity() instanceof Relationship) {
            Relationship relationship = record.get(0).asRelationship();
            ResultObject resultObject = new ResultObject();
            resultObject.setStart(relationship.startNodeId());
            resultObject.setEnd(relationship.endNodeId());
            resultObject.setProperties(relationship.asMap());
            resultObject.setType(relationship.type());
            resultObject.setId(relationship.id());
            resultObject.setResType(ResType.Edge);
            return resultObject;
        }

        if(record.get(0).asEntity() instanceof Node) {
            Node node = record.get(0).asNode();
            ResultObject resultObject = new ResultObject();

            if(node.containsKey("start")) {
                resultObject.setStart(node.get("start").asLong());
            }

            if(node.containsKey("end")) {
                resultObject.setEnd(node.get("end").asLong());
            }

            resultObject.setProperties(node.asMap());

            if(node.labels().iterator().hasNext()) {
                resultObject.setType(node.labels().iterator().next());
            } else {
                resultObject.setType("unspecified");
            }

            resultObject.setId(node.id());
            resultObject.setResType(ResType.Node);
            return resultObject;
        }
        throw new IllegalStateException("No node or relationship");
    }

    public boolean isConnected() {
        return driver != null && driver.session().isOpen();
    }


    private Long firstResultAsLong(Record record) {
        return record.get(0).asLong();
    }

    private  <T> Neo4jGraph<T> execute(CypherQuery cypherQuery, Function<Record, T> fn) {
        try (Session session = driver.session()) {
            StatementResult statementResult = session.run(cypherQuery.getQuery(), cypherQuery.getParams());
            List<T> list = statementResult.list(fn::apply);
            return new Neo4jGraph<>(list);
        }
    }
}
