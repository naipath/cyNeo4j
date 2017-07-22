package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.provider;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.CypherQueryTemplate;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.reader.CypherQueryTemplateReader;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.reader.CypherQueryTemplateReaderException;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.*;

//TODO: load templates form directory
public class CypherQueryTemplateResourceProvider implements QueryTemplateProvider {

    private static Logger LOG = Logger.getLogger(CypherQueryTemplateResourceProvider.class);
    private static String[] templateFiles = new String[] { "/gene-detail.xml", "/gene-path.xml"};
    private Map<Long, CypherQueryTemplate> cypherQueryTemplateList = new HashMap<>();

    public static CypherQueryTemplateResourceProvider create() {
        CypherQueryTemplateResourceProvider provider = new CypherQueryTemplateResourceProvider();
        CypherQueryTemplateReader reader = new CypherQueryTemplateReader();
        long id = 0;
        for(String path : templateFiles) {
            InputStream in = CypherQueryTemplateResourceProvider.class.getResourceAsStream(path);
            if(in != null) {
                try {
                    CypherQueryTemplate template = reader.read(in);
                    provider.cypherQueryTemplateList.put( (id++) ,template);
                } catch (CypherQueryTemplateReaderException e) {
                    LOG.warn("Cannot load query template file: " + e.getMessage());
                }
            }
        }
        return provider;
    }

    private CypherQueryTemplateResourceProvider() {

    }

    public Map<Long, CypherQueryTemplate> getCypherQueryTemplateMap() {
        return cypherQueryTemplateList;
    }

    public Optional<CypherQueryTemplate> getCypherQueryTemplate(Long id) {
        return Optional.ofNullable(cypherQueryTemplateList.get(id));
    }
}
