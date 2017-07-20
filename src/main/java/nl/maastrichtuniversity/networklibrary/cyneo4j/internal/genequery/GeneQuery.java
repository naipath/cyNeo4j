package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.genequery;

public abstract class GeneQuery {

    public abstract <Q> Q accept(GeneSchema<Q> geneSchema);

    public void select(GeneId geneId) {

    }

    public void select(TranscriptId transcriptId) {

    }

    public void select(GeneId geneId, int numberOfNeighbours, boolean withGeneNetwork) {

    }


}
