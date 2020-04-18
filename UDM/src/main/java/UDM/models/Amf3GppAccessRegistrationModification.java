package UDM.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Amf3GppAccessRegistrationModification {
    private enum ImsVoPS{HOMOGENEOUS_SUPPORT,
                        HOMOGENEOUS_NON_SUPPORT,
                        NON_HOMOGENEOUS_OR_UNKNOWN}
    private ImsVoPS imsVoPS;
    private boolean purgeFlag;
    private int pei;

    public Amf3GppAccessRegistrationModification() {
    }

    public Amf3GppAccessRegistrationModification(ImsVoPS imsVoPS, boolean purgeFlag, int pei) {

        this.imsVoPS = imsVoPS;
        this.purgeFlag = purgeFlag;
        this.pei = pei;
    }

    public ImsVoPS getImsVoPS() {
        return imsVoPS;
    }

    public void setImsVoPS(ImsVoPS imsVoPS) {
        this.imsVoPS = imsVoPS;
    }

    public boolean isPurgeFlag() {
        return purgeFlag;
    }

    public void setPurgeFlag(boolean purgeFlag) {
        this.purgeFlag = purgeFlag;
    }

    public int getPei() {
        return pei;
    }

    public void setPei(int pei) {
        this.pei = pei;
    }
}
