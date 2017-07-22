package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate.reader;

import javax.xml.bind.annotation.XmlEnum;

@XmlEnum
public enum CyQueryInteraction {
    DIRECTED,
    UNDIRECTED,
    INCOMING,
    OUTGOING
}
