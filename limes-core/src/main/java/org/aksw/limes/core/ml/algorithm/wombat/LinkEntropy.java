package org.aksw.limes.core.ml.algorithm.wombat;

public class LinkEntropy implements Comparable<LinkEntropy>{
    
    protected String sourceUri;
    protected String targetUri;
    protected double entropy;
    
    
    public LinkEntropy(String sourceUri, String targetUri, double confidence) {
        super();
        this.sourceUri = sourceUri;
        this.targetUri = targetUri;
        this.entropy = confidence;
    }
    
    public String getSourceUri() {
        return sourceUri;
    }
    public void setSourceUri(String sourceUri) {
        this.sourceUri = sourceUri;
    }
    public String getTargetUri() {
        return targetUri;
    }
    public void setTargetUri(String targetUri) {
        this.targetUri = targetUri;
    }
    public double getEntropy() {
        return entropy;
    }
    public void setEntropy(int entropy) {
        this.entropy = entropy;
    }

    @Override
    public int compareTo(LinkEntropy o) {
        double diff = entropy - o.entropy;
        if(diff < 0){
            return -1;
        }else if(diff > 0){
            return 1;
        }
        return 0;
    }
    
    
}