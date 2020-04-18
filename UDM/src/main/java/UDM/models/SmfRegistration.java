package UDM.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SmfRegistration {
    public long smfId;
    public long pduSessionId;
    public String dnn;

    public long getSmfId() {
        return smfId;
    }

    public void setSmfId(long smfId) {
        this.smfId = smfId;
    }

    public long getPduSessionId() {
        return pduSessionId;
    }

    public void setPduSessionId(long pduSessionId) {
        this.pduSessionId = pduSessionId;
    }

    public String getDnn() {
        return dnn;
    }

    public void setDnn(String dnn) {
        this.dnn = dnn;
    }

    public SmfRegistration(long smfId, long pduSessionId, String dnn) {
        this.smfId = smfId;
        this.pduSessionId = pduSessionId;
        this.dnn = dnn;
    }

    public SmfRegistration() {
    }
}
