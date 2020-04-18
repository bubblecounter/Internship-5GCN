package SMF.models;

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

    @Override
    public String toString() {
        return "SMContextCreatedData{" +
                "pduSessionId=" + pduSessionId +
                '}';
    }

    public SMContextCreatedData(long pduSessionId) {
        this.pduSessionId = pduSessionId;
    }
}
