package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.reader;

import javax.xml.bind.JAXBException;

public class ReaderException extends Exception {
    public ReaderException(String msg) {
        super(msg);
    }

    public ReaderException(String msg, Throwable e) {
        super(msg, e);
    }
}
