package si.fri.rso.skupina09.services.properties;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ConfigBundle("rest-properties")
@ApplicationScoped
public class RestProperties {

    private Boolean simulation;

    private String izdelki;

    public Boolean getSimulation() {
        return simulation;
    }

    public void setSimulation(Boolean simulation) {
        this.simulation = simulation;
    }

    public String getIzdelki() {
        return izdelki;
    }

    public void setIzdelki(String izdelki) {
        this.izdelki = izdelki;
    }
}