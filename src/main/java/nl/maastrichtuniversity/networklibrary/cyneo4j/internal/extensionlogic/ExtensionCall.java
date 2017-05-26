package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic;

public interface ExtensionCall {
    void setUrlFragment(String urlFragment);

    void setPayload(String payload);

    void setAsync(boolean async);

    /**
     * @return
     */
    String getUrlFragment();

    String getPayload();

    boolean isAsync();
}
