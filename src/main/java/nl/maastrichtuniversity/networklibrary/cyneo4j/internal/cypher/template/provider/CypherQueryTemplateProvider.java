package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.template.provider;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.template.CypherQueryTemplate;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.template.reader.CypherQueryTemplateReader;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.template.reader.CypherQueryTemplateReaderException;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//TODO: load templates form directory
public class CypherQueryTemplateProvider {

    private static Logger LOG = Logger.getLogger(CypherQueryTemplateProvider.class);
    private static String[] templateFiles = new String[] { "/gene-detail.xml", "/gene-path.xml"};
    private List<CypherQueryTemplate> cypherQueryTemplateList = new ArrayList<>();

    public static CypherQueryTemplateProvider create() {
        CypherQueryTemplateProvider provider = new CypherQueryTemplateProvider();
        CypherQueryTemplateReader reader = new CypherQueryTemplateReader();
        for(String path : templateFiles) {
            InputStream in = CypherQueryTemplateProvider.class.getResourceAsStream(path);
            if(in != null) {
                try {
                    CypherQueryTemplate template = reader.read(in);
                    provider.cypherQueryTemplateList.add(template);
                } catch (CypherQueryTemplateReaderException e) {
                    LOG.warn("Cannot load query template file: " + e.getMessage());
                }
            }
        }
        return provider;
    }

    private CypherQueryTemplateProvider() {

    }

    public List<CypherQueryTemplate> getCypherQueryTemplateList() {
        return cypherQueryTemplateList;
    }

    public Optional<CypherQueryTemplate> getCypherQueryTemplate(String name) {
        return cypherQueryTemplateList.stream().filter(q -> q.getName().equals(name)).findFirst();
    }
}
