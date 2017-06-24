package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.bioquery;

public class Gene {
    private final SequenceId sequenceId;
    private final GeneId geneId;
    private final int start, end;
    private final CodingSequenceId codingSequenceId;

    public Gene(SequenceId sequenceId, GeneId geneId, int start, int end, CodingSequenceId codingSequenceId) {
        this.sequenceId = sequenceId;
        this.geneId = geneId;
        this.start = start;
        this.end = end;
        this.codingSequenceId = codingSequenceId;
    }
}
