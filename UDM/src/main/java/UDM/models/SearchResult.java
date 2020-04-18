package UDM.models;


import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
@XmlRootElement
public class SearchResult {
    public ArrayList<NFProfile> nfInstances;
    public int validityPeriod;

    public ArrayList<NFProfile> getNfInstances() {
        return nfInstances;
    }

    public void setNfInstances(ArrayList<NFProfile> nfInstances) {
        this.nfInstances = nfInstances;
    }

    public int getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(int validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "nfInstances=" + nfInstances +
                ", validityPeriod=" + validityPeriod +
                '}';
    }

    public SearchResult(ArrayList<NFProfile> nfInstances, int validityPeriod) {
        this.nfInstances = nfInstances;
        this.validityPeriod = validityPeriod;
    }

    public SearchResult() {
    }
}
