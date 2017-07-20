package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.genequery;

public class Transcript {
    private final TranscriptId transcriptId;
    private final SequenceId sequenceId;
    private final GeneId geneId;
    private final int start, end;


    public Transcript(TranscriptId transcriptId, SequenceId sequenceId, GeneId geneId, int start, int end) {
        this.transcriptId = transcriptId;
        this.sequenceId = sequenceId;
        this.geneId = geneId;
        this.start = start;
        this.end = end;
    }
}
