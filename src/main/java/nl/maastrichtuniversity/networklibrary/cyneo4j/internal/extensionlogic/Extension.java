package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.extensionlogic;

import java.util.List;

public interface Extension {

	/**
	 * The name of the extension as used by the server providing it
	 * @return
	 */
	String 	getName();
	
	/**
	 * Textual description of the service
	 * @return
	 */
	String 	getDescription();
	
	/**
	 * The relative location of the extension in regards to the server.
	 * @return 
	 */
	String 	getEndpoint();
	
	void 	setName(String name);
	void 	setDescription(String desc);
	void 	setEndpoint(String endpoint);
	
	/**
	 * All parameters that can be supplied to the extension. The order is not relevant,
	 * Neo4j takes care of the mapping.
	 * @return
	 */
	List<ExtensionParameter> 	getParameters();
	void 	setParameters(List<ExtensionParameter> params);
}
