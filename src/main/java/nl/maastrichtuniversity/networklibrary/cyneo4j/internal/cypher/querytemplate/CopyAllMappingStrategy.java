package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.cypher.querytemplate;

public class CopyAllMappingStrategy implements MappingStrategy {

    private final String referenceColumn;
    private final String networkName;

    public CopyAllMappingStrategy(String referenceColumn, String networkName) {
        this.referenceColumn = referenceColumn;
        this.networkName = networkName;
    }

    public String getReferenceColumn() {
        return referenceColumn;
    }

    public String getNetworkName() {
        return networkName;
    }

    @Override
    public void accept(MappingStrategyVisitor visitor) {
        visitor.visit(this);
    }
}
