package Services;

import Entities.Memberformfield;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static javax.persistence.PersistenceContextType.TRANSACTION;

/**
 * Created by Dziugas on 4/25/2016.
 */
@Stateless
public class MemberFormService {
    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU", type=TRANSACTION)
    private EntityManager em;

    public void editFields(Memberformfield[] fields){
        for (Memberformfield field : fields) {
            em.merge(field);
        }
    }
}
