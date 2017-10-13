package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.reader;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.CypherQueryTemplate;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.mapping.GraphMapping;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ReaderTest {
    @Test
    public void read() throws Exception {
        Reader reader = new Reader();
        CypherQueryTemplate template = reader.read(getClass().getResourceAsStream("/gene-detail.xml"));
        assertNotNull(template);
        assertTrue(template.getMapping() instanceof GraphMapping);
        assertTrue(((GraphMapping)template.getMapping()).getNodeColumnMapping().size() >0 );
        assertTrue(((GraphMapping)template.getMapping()).getEdgeColumnMapping().size() >0 );
        assertNotNull(((GraphMapping)template.getMapping()).getNodeReferenceIdColumn());
        assertNotNull(((GraphMapping)template.getMapping()).getEdgeReferenceIdColumn());
    }

}