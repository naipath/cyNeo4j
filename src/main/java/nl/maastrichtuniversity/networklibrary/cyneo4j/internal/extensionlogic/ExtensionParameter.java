package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic;

/**
 * This interface describes a parameter for the call of an extension.
 *
 * @author gsu
 */
public interface ExtensionParameter {
    String getName();

    String getDescription();

    boolean isOptional();

    Class<?> getType();

    void setType(Class<?> type);

    void setName(String name);

    void setDescription(String description);

    void setOptional(boolean optional);

}
