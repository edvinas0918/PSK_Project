package Services;

import Entities.Clubmember;
import Entities.Memberstatus;
import Entities.Payment;
import Entities.Settings;
import Helpers.InsufficientFundsException;
import Interceptors.Audit;
import RestControllers.ClubmemberFacadeREST;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;

import java.util.Calendar;
import java.util.List;

import static javax.persistence.PersistenceContextType.TRANSACTION;

/**
 * Created by Dziugas on 4/25/2016.
 */
@Stateless
public class ClubMemberService {
    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU", type=TRANSACTION)
    private EntityManager em;

    @Context
    HttpServletRequest webRequest;

    @Inject
    SettingsService settingsService;

    @Inject
    EmailService emailService;

    @Inject
    IPaymentService paymentService;

    @Inject
    ClubmemberFacadeREST memberFacade;

    public Clubmember getMember(Integer id){
        return em.find(Clubmember.class, id);
    }

    public List<Clubmember> getAllMembers() {
        return em.createNamedQuery("Clubmember.findAll").getResultList();
    }

    public void updateMember(Clubmember member) {
        em.merge(member);
    }

    @Audit
    public void grantPoints(Clubmember member, Integer points){
        if (points > 0){
            member.setPoints(member.getPoints() + points);
            em.persist(member);
        }
    }

    public void renewMembership(Clubmember member) throws InsufficientFundsException {
        //Adding year to membership year
        Calendar c = Calendar.getInstance();
        if (member.getMembershipExpirationDate() != null)
            c.setTime(member.getMembershipExpirationDate());
        c.add(Calendar.YEAR, 1);
        member.setMembershipExpirationDate(c.getTime());
        int price = Integer.parseInt(em.createNamedQuery("Settings.findByReferenceCode", Settings.class).
                setParameter("referenceCode", "memberTax").getSingleResult().getValue());

        paymentService.makePayment(member, price, "Narystės mokestis");
    }

    public void recommendCandidate(int candidateId){
        Clubmember currentUser = getCurrentUser();
        Clubmember candidate = getMember(candidateId);
        if (currentUser.getRecommendedMembers().contains(candidate)){
            return;
        }
        currentUser.getRecommendedMembers().add(candidate);
        candidate.getRecommenders().add(currentUser);

        Integer requiredRecommendations = Integer.parseInt(settingsService.getSetting("recommendationsMin").getValue());
        if (candidate.getRecommenders().size() >= requiredRecommendations){
            promoteCandidate(candidate);
        }

        updateMember(currentUser);
        updateMember(candidate);
    }

    private void promoteCandidate(Clubmember candidate){
        candidate.setMemberStatus(getMemberStatusByName("Member"));

        try {
            emailService.sendCandidatePromotionEmail(candidate.getEmail());
        } catch(Exception e){ }
    }

    public Clubmember getCurrentUser(){
        Clubmember user = (Clubmember)webRequest.getSession().getAttribute("User");

        if (user != null) {
            return memberFacade.find(user.getId());
        }

        return null;
    }

    public Memberstatus getMemberStatusByName(String name){
        return (Memberstatus)em.createNamedQuery("Memberstatus.findByName")
                .setParameter("name", name).getSingleResult();
    }

    public void DeactivateMember (int id){
        Clubmember member = em.find(Clubmember.class, id);
        member.setIsActive(false);
    }

    public int countActiveMembers() {
        return em.createNamedQuery("Clubmember.findAll").getResultList().size();
    }
}
