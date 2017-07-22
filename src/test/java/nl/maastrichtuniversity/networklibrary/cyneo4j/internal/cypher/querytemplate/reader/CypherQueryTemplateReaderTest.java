package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.reader;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.CypherQueryTemplate;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

public class CypherQueryTemplateReaderTest {
    @Test
    public void read() throws Exception {

        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("gene-detail.xml");
        CypherQueryTemplateReader reader = new CypherQueryTemplateReader();
        CypherQueryTemplate cypherQueryTemplate = reader.read(inputStream);

        assertNotNull(cypherQueryTemplate);
        assertNotNull(cypherQueryTemplate.getName());
        assertNotNull(cypherQueryTemplate.getMapping().getEdgeMapping());
        assertNotNull(cypherQueryTemplate.getMapping().getNodeMapping());
    }

}