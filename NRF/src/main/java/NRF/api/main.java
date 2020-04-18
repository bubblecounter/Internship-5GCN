package NRF.api;
// In order to run this, you need the alpn-boot-XXX.jar in the bootstrap classpath.
public class main {

    public static void main(String[] args) {
        long nrf1Id = 352658555555555L;
        int nrf1PortNumber = 8443;
        NRF Nrf1 = new NRF(nrf1Id,nrf1PortNumber);
        Nrf1.connect();
    }
}
