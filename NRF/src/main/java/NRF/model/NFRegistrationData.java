package NRF.model;

import NRF.model.NFProfile;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NFRegistrationData {
    public int heartBeatTimer;
    public NFProfile nfProfile;

    public int getHeartBeatTimer() {
        return heartBeatTimer;
    }

    public void setHeartBeatTimer(int heartBeatTimer) {
        this.heartBeatTimer = heartBeatTimer;
    }

    public NFProfile getNfProfile() {
        return nfProfile;
    }

    public void setNfProfile(NFProfile nfProfile) {
        this.nfProfile = nfProfile;
    }

    public NFRegistrationData(int heartBeatTimer, NFProfile nfProfile) {
        this.heartBeatTimer = heartBeatTimer;
        this.nfProfile = nfProfile;
    }

    @Override
    public String toString() {
        return "NFRegistrationData{" +
                "heartBeatTimer=" + heartBeatTimer +
                ", nfProfile=" + nfProfile.toString() +
                '}';
    }

    public NFRegistrationData() {
    }
}
