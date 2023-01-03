package si.fri.rso.skupina09.services.beans;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import si.fri.rso.skupina09.lib.Izdelek;
import si.fri.rso.skupina09.services.properties.RestProperties;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RequestScoped
public class FaultToleranceBean {

    private Logger logger = Logger.getLogger(FaultToleranceBean.class.getName());

    private Client httpClient;

    @Inject
    RestProperties restProperties;

    @Timeout(value = 5, unit = ChronoUnit.SECONDS)
    @Fallback(fallbackMethod = "fallback")
    public List<Izdelek> vrniIzdelke_fault_tolerance_fallback(){

        logger.info("SEM V METODI vrniIzdelke_fault_tolerance_fallback_only");

        List<Izdelek> response = new ArrayList<>();

        if (restProperties.getSimulation()){
            throw new InternalServerErrorException();

        }
        httpClient = ClientBuilder.newClient();
        try {
            response = httpClient.target(restProperties.getIzdelki() + "/izdelki").request(MediaType.APPLICATION_JSON).get(List.class);
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
        return response;
    }
    public List<Izdelek> fallback() {
        logger.info("HI FROM FALLBACK");
        return new ArrayList<>();
    }

    @CircuitBreaker(delay=10000, requestVolumeThreshold = 1, failureRatio = 0.5, successThreshold = 1)
    @Fallback(fallbackMethod = "fallback_circuit")
    public List<Izdelek> vrniIzdelke_fault_tolerance_circuit_and_timeout() {

        logger.info("SEM V METODI vrniIzdelke_fault_tolerance_circuit_and_timeout");

        if (restProperties.getSimulation()){
            throw new InternalServerErrorException();
        }
        List<Izdelek> response = new ArrayList<>();
        httpClient = ClientBuilder.newClient();
        try {
            response = httpClient.target(restProperties.getIzdelki() + "/izdelki").request(MediaType.APPLICATION_JSON).get(List.class);
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
        return response;
    }

    public List<Izdelek> fallback_circuit() {
        logger.info("HI FROM FALLBACK CIRCUIT BREAKER");
        return new ArrayList<>();
    }
}
