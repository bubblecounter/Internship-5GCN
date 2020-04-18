package UDM.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DeregistrationData {


    private String deregistrationReason;

    public String getDeregistrationReason() {
        return deregistrationReason;
    }

    public void setDeregistrationReason(String deregistrationReason) {
        this.deregistrationReason = deregistrationReason;
    }

    public DeregistrationData() {

    }

    @Override
    public String toString() {
        return "DeregistrationData{" +
                "deregistrationReason='" + deregistrationReason + '\'' +
                '}';
    }

    public DeregistrationData(String deregistrationReason) {
        this.deregistrationReason = deregistrationReason;
    }

}
