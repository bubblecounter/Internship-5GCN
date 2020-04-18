package UDM.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Amf3GppAccessRegistration {

    private long amfId;
    private long pei;
    private String deregCallbackUri = "hello";

    public Amf3GppAccessRegistration(long amfId, long pei, String deregCallbackUri) {
        this.amfId = amfId;
        this.pei = pei;
        this.deregCallbackUri = deregCallbackUri;
    }

    public Amf3GppAccessRegistration() {
    }

    public String getDeregCallbackUri() {
        return deregCallbackUri;
    }

    public void setDeregCallbackUri(String deregCallbackUri) {
        this.deregCallbackUri = deregCallbackUri;
    }

    public long getPei() {
        return pei;
    }

    public void setPei(long pei) {
        this.pei = pei;
    }

    public long getAmfId() {
        return amfId;
    }

    public void setAmfId(long amfId) {
        this.amfId = amfId;
    }

    @Override
    public String toString() {
        return "Amf3GppAccessRegistration{" +
                "amfId=" + amfId +
                ", pei=" + pei +
                ", deregCallbackUri='" + deregCallbackUri + '\'' +
                '}';
    }


}
