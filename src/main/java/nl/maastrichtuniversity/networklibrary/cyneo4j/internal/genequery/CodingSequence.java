package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.genequery;

public class CodingSequence {
    private final SequenceId sequenceId;
    private final GeneId geneId;
    private final int start, end;

    public CodingSequence(SequenceId sequenceId, GeneId geneId, int start, int end) {
        this.sequenceId = sequenceId;
        this.geneId = geneId;
        this.start = start;
        this.end = end;
    }
}
