package NRF.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SubscriptionData {
    public String nfStatusNotificationUri;

    public String getNfStatusNotificationUri() {
        return nfStatusNotificationUri;
    }

    public void setNfStatusNotificationUri(String nfStatusNotificationUri) {
        this.nfStatusNotificationUri = nfStatusNotificationUri;
    }

    public SubscriptionData(String nfStatusNotificationUri) {
        this.nfStatusNotificationUri = nfStatusNotificationUri;
    }

    @Override
    public String toString() {
        return "SubscriptionData{" +
                "nfStatusNotificationUri='" + nfStatusNotificationUri + '\'' +
                '}';
    }

    public SubscriptionData() {
    }
}
