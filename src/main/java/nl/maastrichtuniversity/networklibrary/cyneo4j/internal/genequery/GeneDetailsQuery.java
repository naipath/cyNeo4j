package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.genequery;

import java.util.List;

public class GeneDetailsQuery extends GeneQuery {
    private final List<GeneId> geneIdList;

    public GeneDetailsQuery(List<GeneId> geneIdList) {
        this.geneIdList = geneIdList;
    }
    public List<GeneId> getGeneIdList() {
        return geneIdList;
    }

    @Override
    public <Q> Q accept(GeneSchema<Q> geneSchema) {
        return geneSchema.createQuery(this);
    }
}
