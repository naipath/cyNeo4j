package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.provider;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.CypherQueryTemplate;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.reader.CypherQueryTemplateReader;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.reader.CypherQueryTemplateReaderException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.*;

public class CypherQueryTemplateDirectoryProvider implements QueryTemplateProvider {
//    private static Logger LOG = Logger.getLogger(CypherQueryTemplateResourceProvider.class);

    private Map<Long, CypherQueryTemplate> cypherQueryTemplateMap = new HashMap<>();

    public static CypherQueryTemplateDirectoryProvider create() {
        return new CypherQueryTemplateDirectoryProvider();
    }

    public static CypherQueryTemplateDirectoryProvider create(Path path) {
        CypherQueryTemplateDirectoryProvider provider = new CypherQueryTemplateDirectoryProvider();
        CypherQueryTemplateReader reader = new CypherQueryTemplateReader();
        try(DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path, "*.xml")) {
            long id = 0;
            for(Path filePath : directoryStream) {
                CypherQueryTemplate queryTemplate = parseTemplateQueryXml(reader, filePath);
                if(queryTemplate != null) {
                    provider.cypherQueryTemplateMap.put(id++, queryTemplate );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return provider;
    }

    private static CypherQueryTemplate parseTemplateQueryXml(CypherQueryTemplateReader reader, Path filePath) throws IOException {
        InputStream in = Files.newInputStream(filePath, StandardOpenOption.READ);
        if(in != null) {
            try {
                return reader.read(in);
            } catch (CypherQueryTemplateReaderException e) {
//                LOG.warn("Cannot parse query template file: " + filePath.toAbsolutePath().toString() + " : " + e.getMessage());
            }
        }
//        LOG.warn("Cannot read file: " + filePath.toAbsolutePath().toString());
        return null;
    }

    private CypherQueryTemplateDirectoryProvider() {

    }

    @Override
    public Map<Long, CypherQueryTemplate> getCypherQueryTemplateMap() {
        return cypherQueryTemplateMap;
    }

    @Override
    public Optional<CypherQueryTemplate> getCypherQueryTemplate(Long id) {
        if(cypherQueryTemplateMap.containsKey(id)) {
            return Optional.of(cypherQueryTemplateMap.get(id));
        } else {
            return Optional.empty();
        }
    }

    public void readDirectory(Path templateDirectory) {
        this.cypherQueryTemplateMap.clear();
        CypherQueryTemplateReader reader = new CypherQueryTemplateReader();
        try(DirectoryStream<Path> directoryStream = Files.newDirectoryStream(templateDirectory, "*.xml")) {
            long id = 0;
            for(Path filePath : directoryStream) {
                CypherQueryTemplate queryTemplate = parseTemplateQueryXml(reader, filePath);
                if(queryTemplate != null) {
                    this.cypherQueryTemplateMap.put(id++, queryTemplate );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
