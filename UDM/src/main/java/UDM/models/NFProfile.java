package UDM.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NFProfile {
    public long nfInstanceID;
    public String nfType;
    public String ipv4Address;

    public long getNfInstanceID() {
        return nfInstanceID;
    }

    public void setNfInstanceID(long nfInstanceID) {
        this.nfInstanceID = nfInstanceID;
    }

    public String getNfType() {
        return nfType;
    }

    public void setNfType(String nfType) {
        this.nfType = nfType;
    }

    public String getIpv4Address() {
        return ipv4Address;
    }

    public void setIpv4Address(String ipv4Address) {
        this.ipv4Address = ipv4Address;
    }

    public NFProfile(long nfInstanceID, String nfType, String ipv4Address) {
        this.nfInstanceID = nfInstanceID;
        this.nfType = nfType;
        this.ipv4Address = ipv4Address;
    }

    public NFProfile() {
    }
}
