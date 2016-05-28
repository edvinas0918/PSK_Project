package restControllers;

import entities.Clubmember;
import entities.Invitation;
import services.ClubMemberService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Aurimas on 2016-05-19.
 */
@Stateless
@Path("invitation")
public class InvitationFacadeRest extends AbstractFacade<Invitation>{

    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Inject
    private ClubMemberService clubMemberService;

    public InvitationFacadeRest() {
        super(Invitation.class);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Invitation> findByMemberId() {
        return em.find(Clubmember.class,  clubMemberService.getCurrentUser().getId()).getInvitationList() ;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
