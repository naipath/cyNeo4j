package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.genequery;

public class Intron {
    private final SequenceId sequenceId;
    private final GeneId geneId;
    private final int start, end;

    public Intron(SequenceId sequenceId, GeneId geneId, int start, int end) {
        this.sequenceId = sequenceId;
        this.geneId = geneId;
        this.start = start;
        this.end = end;
    }
}
