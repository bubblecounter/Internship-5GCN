package SMF.models;

import org.glassfish.jersey.server.Uri;

import javax.json.bind.annotation.JsonbPropertyOrder;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@JsonbPropertyOrder({"title", "status", "cause","type","instance","invalidParams"})
@XmlType(propOrder={"title", "status", "cause","type","instance","invalidParams"})
public class ProblemDetails {
    public Uri type;
    public String title;
    public int status;
    public String instance;
    public String cause;
    public List<param> invalidParams = new ArrayList<param>();

    @Override
    public String toString() {
        return "ProblemDetails{" +
                "type=" + type +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", instance='" + instance + '\'' +
                ", cause='" + cause + '\'' +
                ", invalidParams=" + invalidParams +
                '}';
    }

    public Uri getType() {
        return type;
    }

    public void setType(Uri type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public List<param> getInvalidParams() {
        return invalidParams;
    }

    public void setInvalidParams(List<param> invalidParams) {
        this.invalidParams = invalidParams;
    }

    public void addParam(String param, String reason){
        param p = new param(param,reason);
        invalidParams.add(p);
    }

    public class param{
        public String param;
        public String reason;

        public param(String param, String reason) {
            this.param = param;
            this.reason = reason;
        }
    }
}
