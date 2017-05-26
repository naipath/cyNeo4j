package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic;

/**
 * This interface describes a parameter for the call of an extension.
 *
 * @author gsu
 */
public interface ExtensionParameter {
    String getName();

    boolean isOptional();
}
