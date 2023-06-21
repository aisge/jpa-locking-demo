package at.htl.boundary;

import at.htl.model.Person;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

@Path("/optimistic/")
public class OptimisticResource {

    @Inject
    Logger logger;

    @Inject
    EntityManager entityManager;

    @GET
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Path("read/{name}")
    public Person read(@PathParam("name") String lastname) {
        var query = entityManager.createQuery("select p from Person p where lower(lastname)=lower(:lastname)", Person.class);
        query.setParameter("lastname", lastname);
        Person p = query.getSingleResult();

        logger.info("OPTIMISTIC: " + p);
        try { Thread.sleep(30000); } catch (InterruptedException ioe) {}

        return p;
    }


    @GET
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Path("write/{name}")
    public Person write(@PathParam("name") String lastname) {
        var query = entityManager.createQuery("select p from Person p where lower(lastname)=lower(:lastname)", Person.class);
        query.setParameter("lastname", lastname);
        Person p = query.getSingleResult();

        logger.info("OPTIMISTIC_FORCE_INCREMENT: " + p);
        try { Thread.sleep(30000); } catch (InterruptedException ioe) {}

        return p;
    }

}
