package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.reader;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.CypherQueryTemplate;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReaderTest {
    @Test
    public void read() throws Exception {
        Reader reader = new Reader();
        CypherQueryTemplate template = reader.read(getClass().getResourceAsStream("/gene-detail.xml"));
        assertNotNull(template);
        assertTrue(template.getMapping().getNodeColumnMapping().size() >0 );
        assertTrue(template.getMapping().getEdgeColumnMapping().size() >0 );
        assertNotNull(template.getMapping().getNodeReferenceIdColumn());
        assertNotNull(template.getMapping().getEdgeReferenceIdColumn());
    }

}