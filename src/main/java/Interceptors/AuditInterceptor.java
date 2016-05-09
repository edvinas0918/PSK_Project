package Interceptors;

import Entities.Clubmember;
import Entities.Moneyoperationlogentry;
import Services.ClubMemberService;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;

import java.lang.reflect.Method;
import java.util.Date;

import static javax.persistence.PersistenceContextType.TRANSACTION;

/**
 * Created by Dziugas on 4/30/2016.
 */
@Interceptor
@Audit
public class AuditInterceptor {

    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU", type=TRANSACTION)
    private EntityManager em;

    @Context
    HttpServletRequest webRequest;

    @Inject
    ClubMemberService memberService;

    @AroundInvoke
    public Object logMoneyOperationInfo(InvocationContext ctx) throws Exception {
        Moneyoperationlogentry logEntry = new Moneyoperationlogentry();

        HttpSession session = webRequest.getSession();
        Clubmember member = (Clubmember)session.getAttribute("User");
        logEntry.setMember(member);
        logEntry.setMemberFirstName(member.getFirstName());
        logEntry.setMemberLastName(member.getLastName());
        logEntry.setMemberStatus(member.getMemberStatus());
        Method method = ctx.getMethod();
        logEntry.setInvokedMethod(method.getDeclaringClass().getName() + "." + method.getName());
        logEntry.setOperationTime(new Date());

        em.persist(logEntry);

        return ctx.proceed();
    }
}
