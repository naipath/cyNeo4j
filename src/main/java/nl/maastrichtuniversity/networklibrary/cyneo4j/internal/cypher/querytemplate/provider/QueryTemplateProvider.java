package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.provider;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.CypherQueryTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface QueryTemplateProvider {
    Map<Long, CypherQueryTemplate> getCypherQueryTemplateMap();
    Optional<CypherQueryTemplate> getCypherQueryTemplate(Long id);
}
