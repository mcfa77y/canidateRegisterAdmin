package org.jboss.as.quickstarts.kitchensink.data;

import org.jboss.as.quickstarts.kitchensink.model.Candidate;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@RequestScoped
public class CandidateListProducer {
   @Inject
   private EntityManager em;

   private List<Candidate> candidates;

   // @Named provides access the return value via the EL variable name "candidates" in the UI (e.g.,
   // Facelets or JSP view)
   @Produces
   @Named
   public List<Candidate> getCandidates() {
      return candidates;
   }

   public void onCandidateListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Candidate candidate) {
      retrieveAllCandidatesOrderedByName();
   }

   @PostConstruct
   public void retrieveAllCandidatesOrderedByName() {
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery<Candidate> criteria = cb.createQuery(Candidate.class);
      Root<Candidate> candidate = criteria.from(Candidate.class);
      // Swap criteria statements if you would like to try out type-safe criteria queries, a new
      // feature in JPA 2.0
      // criteria.select(candidate).orderBy(cb.asc(candidate.get(Candidate_.name)));
      criteria.select(candidate).orderBy(cb.asc(candidate.get("name")));
      candidates = em.createQuery(criteria).getResultList();
   }
}
