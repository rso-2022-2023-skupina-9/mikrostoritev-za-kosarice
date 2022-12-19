package si.fri.rso.skupina09.api.v1.resources;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import si.fri.rso.skupina09.lib.Izdelek;
import si.fri.rso.skupina09.lib.Kosarica;
import si.fri.rso.skupina09.services.beans.KosaricaBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@Log
@ApplicationScoped
@Path("/kosarice")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(supportedMethods = "GET, POST, PUT, DELETE")
public class MikrostoritevZaKosariceResource {

    private Logger logger = Logger.getLogger(MikrostoritevZaKosariceResource.class.getName());

    @Inject
    private KosaricaBean kosaricaBean;

    @Context
    protected UriInfo uriInfo;

    @Operation(description = "Pridobi informacije o vseh kosaricah.", summary = "Pridobi vse kosarice")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Seznam vseh kosaric",
                    content = @Content(schema = @Schema(implementation = Kosarica.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Stevilo objektov v seznamu")}
            )
    })
    @GET
    public Response getKosarice() {
        List<Kosarica> kosarice = kosaricaBean.getKosarice(uriInfo);
        return Response.status(Response.Status.OK).entity(kosarice).build();
    }

    @Operation(description = "Pridobi informacije o kosarici.", summary = "Pridobi kosarico")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Informacije o kosarici",
                    content = @Content(
                            schema = @Schema(implementation = Kosarica.class)
                    )
            )
    })
    @GET
    @Path("{kosaricaId}")
    public Response getKosarica(@Parameter(description = "Kosarica ID", required = true)
                               @PathParam("kosaricaId") Integer kosaricaId) {
        Kosarica kosarica = kosaricaBean.getKosarica(kosaricaId);
        if(kosarica == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(kosarica).build();
    }

    @Operation(description = "Dodaj kosarico.", summary = "Dodaj kosarico")
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "Kosarica uspesno dodana."
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Napaka pri preverjanju podatkov."
            )
    })
    @POST
    public Response createKosarica(@RequestBody(
            description = "DTO objekt z informacijami o kosarici",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = Kosarica.class)
            )
    ) Kosarica kosarica) {
        if(kosarica.getIme() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            kosarica = kosaricaBean.createKosarica(kosarica);
        }
        return Response.status(Response.Status.CREATED).entity(kosarica).build();
    }

    @Operation(description = "Posodobi informacije o kosarici.", summary = "Posodobi kosarico")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Kosarica uspesno posodobljena"
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Kosarica ne obstaja"
            )
    })
    @PUT
    @Path("{kosaricaId}")
    public Response putKosarica(@Parameter(description = "Kosarica ID.", required = true)
                               @PathParam("kosaricaId") Integer kosaricaId,
                               @RequestBody(
                                       description = "DTO objekt z informacijami o kosarici.",
                                       required = true,
                                       content = @Content(
                                               schema = @Schema(implementation = Kosarica.class)
                                       )
                               ) Kosarica kosarica) {
        kosarica = kosaricaBean.putKosarica(kosaricaId, kosarica);
        if(kosarica == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(kosarica).build();
    }

    @Operation(description = "Izbrisi informacije o kosarici.", summary = "Izbrisi kosarico")
    @APIResponses({
            @APIResponse(
                    responseCode = "204",
                    description = "Kosarica uspesno izbrisana"
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Kosarica ne obstaja"
            )
    })
    @DELETE
    @Path("{kosaricaId}")
    public Response deleteKosarica(@Parameter(description = "Kosarica ID.", required = true)
                                  @PathParam("kosaricaId") Integer kosaricaId) {
        boolean deleted = kosaricaBean.deleteKosarica(kosaricaId);
        if(deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Operation(description = "Dodaj izdelek v kosarico.", summary = "Dodajanje izdelka v kosarico")
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "Izdelek uspesno dodan v kosarico"
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Kosarica oz. izdelek ne obstaja"
            )
    })
    @POST
    @Path("{kosaricaId}/izdelki")
    public Response addIzdelekToKosarica(@Parameter(description = "Kosarica ID.", required = true)
                                         @PathParam("kosaricaId") Integer kosaricaId,
                                         @RequestBody(
                                                 description = "Objekt z informacijo o izdelku.",
                                                 required = true,
                                                 content = @Content(
                                                         schema = @Schema(implementation = Izdelek.class)
                                                 )
                                         ) Izdelek izdelek) {
        Kosarica kosarica = kosaricaBean.addIzdelekToKosarica(kosaricaId, izdelek.getIzdelek_id());
        if(kosarica == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(kosarica).build();
    }

    @Operation(description = "Izbrisi izdelek iz kosarice.", summary = "Brisanje izdelka iz kosarice")
    @APIResponses({
            @APIResponse(
                    responseCode = "204",
                    description = "Izdelek uspesno izbrisan iz kosarice"
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Kosarica oz. izdelek ne obstaja"
            )
    })
    @DELETE
    @Path("{kosaricaId}/izdelki/{izdelekId}")
    public Response deleteIzdelekFromKosarica(@Parameter(description = "Kosarica ID.", required = true)
                                                  @PathParam("kosaricaId") Integer kosaricaId,
                                              @Parameter(description = "Izdelek ID.", required = true)
                                              @PathParam("izdelekId") Integer izdelekId
                                         ) {
        Kosarica kosarica = kosaricaBean.deleteIzdelekFromKosarica(kosaricaId, izdelekId);
        if(kosarica == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.NO_CONTENT).entity(kosarica).build();
    }

    @Operation(description = "Izracunaj skupno ceno kosarice.", summary = "Izracunaj ceno kosarice")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Cena uspesno izracunana"
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Kosarica ne obstaja"
            )
    })
    @GET
    @Path("{kosaricaId}/cena")
    public Response calculateCenaKosarice(@Parameter(description = "Kosarica ID.", required = true)
                                              @PathParam("kosaricaId") Integer kosaricaId) {
        Integer cenaKosarice = kosaricaBean.calculateCenaKosarice(kosaricaId);
        if(cenaKosarice == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(String.format("{\"cena\": \"%d\"}", cenaKosarice)).build();
    }
}
