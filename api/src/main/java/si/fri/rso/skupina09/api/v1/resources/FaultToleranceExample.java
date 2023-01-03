package si.fri.rso.skupina09.api.v1.resources;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.logs.cdi.Log;
import si.fri.rso.skupina09.lib.Izdelek;
import si.fri.rso.skupina09.services.beans.FaultToleranceBean;
import si.fri.rso.skupina09.services.properties.RestProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@ApplicationScoped
@Log
@Path("fault-tolerance")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(supportedMethods = "GET, POST, PUT, DELETE, HEAD, OPTIONS")
public class FaultToleranceExample {

    @Context
    protected UriInfo uriInfo;

    @Inject
    private FaultToleranceBean faultToleranceBean;

    @GET
    @Path("fallback")
    public Response fault_tolerance_fallback_demo() {
        List<Izdelek> izdelki = faultToleranceBean.vrniIzdelke_fault_tolerance_fallback();
        return Response.status(Response.Status.OK).entity(izdelki).build();
    }
    @GET
    @Path("circuit-breaker")
    public Response fault_tolerance_circuit_breaker_timeout_demo() {
        List<Izdelek> izdelki = faultToleranceBean.vrniIzdelke_fault_tolerance_circuit_and_timeout();
        return Response.status(Response.Status.OK).entity(izdelki).build();
    }

    @Inject
    private RestProperties restProperties;

    @GET
    @Path("simulate")
    public Response test() {

        restProperties.setSimulation(true);

        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("normal")
    public Response normal() {

        restProperties.setSimulation(false);

        return Response.status(Response.Status.OK).build();
    }
}
