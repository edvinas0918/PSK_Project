package Services;

import Entities.Clubmember;
import Interceptors.Audit;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static javax.persistence.PersistenceContextType.TRANSACTION;

/**
 * Created by Dziugas on 4/25/2016.
 */
@Stateless
public class ClubMemberService {
    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU", type=TRANSACTION)
    private EntityManager em;

    public Clubmember getMember(Integer id){
        return em.find(Clubmember.class, id);
    }

    @Audit
    public void grantPoints(Clubmember member, Integer points){
        if (points > 0){
            member.setPoints(member.getPoints() + points);
            em.persist(member);
        }
    }
}
