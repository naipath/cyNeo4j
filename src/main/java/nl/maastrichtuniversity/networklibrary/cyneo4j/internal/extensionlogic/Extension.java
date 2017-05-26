package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic;

import java.util.List;

public interface Extension {

    String getName();

    String getEndpoint();

    void setName(String name);

    void setEndpoint(String endpoint);

    List<ExtensionParameter> getParameters();

    void setParameters(List<ExtensionParameter> params);
}
