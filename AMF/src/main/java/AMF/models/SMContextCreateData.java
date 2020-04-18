package AMF.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SMContextCreateData {
    long supi;
    long pduSessionId;
    String dnn;
    long servingNfId;
    String anType ;

    String smContextStatusUri;

    public SMContextCreateData() {
    }

    public String getAnType() {
        return anType;
    }

    public void setAnType(String anType) {
        this.anType = anType;
    }

    public long getSupi() {
        return supi;
    }

    public void setSupi(long supi) {
        this.supi = supi;
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

    public long getServingNfId() {
        return servingNfId;
    }

    public void setServingNfId(long servingNfId) {
        this.servingNfId = servingNfId;
    }

    public String getSmContextStatusUri() {
        return smContextStatusUri;
    }

    public void setSmContextStatusUri(String smContextStatusUri) {
        this.smContextStatusUri = smContextStatusUri;
    }

    public SMContextCreateData(long supi, long pduSessionId, String dnn, long servingNfId, String smContextStatusUri) {
        this.supi = supi;
        this.pduSessionId = pduSessionId;
        this.dnn = dnn;
        this.servingNfId = servingNfId;
        this.smContextStatusUri = smContextStatusUri;
    }

    @Override
    public String toString() {
        return "SMContextCreateData{" +
                "supi=" + supi +
                ", pduSessionId=" + pduSessionId +
                ", dnn='" + dnn + '\'' +
                ", servingNfId=" + servingNfId +
                ", anType='" + anType + '\'' +
                ", smContextStatusUri='" + smContextStatusUri + '\'' +
                '}';
    }
}
