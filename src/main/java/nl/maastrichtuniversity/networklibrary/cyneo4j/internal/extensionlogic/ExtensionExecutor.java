package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic;

import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jCall;
import nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic.neo4j.Neo4jExtension;

import java.util.List;

public interface ExtensionExecutor {

    /**
     * Use this method to obtain all necessary parameters for the execution of the extension
     * form the user and/or the environment
     *
     * @return true if the collection is successful, false if any problem arose
     */
    boolean collectParameters();

    /**
     * This method will be called after each extension call is executed by the server.
     *
     * @param call         The call that was executed
     * @param callRetValue The return value generated by the call
     */
    void processCallResponse(Neo4jCall call, Object callRetValue);

    void setPlugin(Plugin plugin);

    void setExtension(Neo4jExtension extension);

    /**
     * Generate here a list of calls that should be executed. Each of the calls is going to be executed
     * sequentially and the call and result will be handed backed to the ExtensionExecutor
     *
     * @return List of calls for the server to execute
     */
    List<Neo4jCall> buildExtensionCalls();

}
