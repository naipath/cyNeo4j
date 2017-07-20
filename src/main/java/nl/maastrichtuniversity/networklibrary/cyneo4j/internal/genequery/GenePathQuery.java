package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.genequery;

public class GenePathQuery extends GeneQuery {
    private final GeneId from;
    private final GeneId to;

    public GenePathQuery(GeneId from, GeneId to) {
        this.from = from;
        this.to = to;
    }

    public GeneId getFrom() {
        return from;
    }

    public GeneId getTo() {
        return to;
    }

    @Override
    public <Q> Q accept(GeneSchema<Q> geneSchema) {
        return geneSchema.createQuery(this);
    }
}
