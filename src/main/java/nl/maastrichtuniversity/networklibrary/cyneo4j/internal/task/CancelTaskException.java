package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.task;

public class CancelTaskException extends RuntimeException {
    public CancelTaskException(String msg) {
        super(msg);
    }
}
