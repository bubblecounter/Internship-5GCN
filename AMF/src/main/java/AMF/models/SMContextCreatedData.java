package AMF.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SMContextCreatedData {
    long pduSessionId;

    public long getPduSessionId() {
        return pduSessionId;
    }

    public void setPduSessionId(long pduSessionId) {
        this.pduSessionId = pduSessionId;
    }

    public SMContextCreatedData() {
    }

    public SMContextCreatedData(long pduSessionId) {
        this.pduSessionId = pduSessionId;
    }

    @Override
    public String toString() {
        return "SMContextCreatedData{" +
                "pduSessionId=" + pduSessionId +
                '}';
    }
}
