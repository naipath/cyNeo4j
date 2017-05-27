package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtParam;

import java.util.List;

public interface Extension {

    String getName();

    String getEndpoint();

    void setName(String name);

    void setEndpoint(String endpoint);

    List<Neo4jExtParam> getParameters();

    void setParameters(List<Neo4jExtParam> params);
}
