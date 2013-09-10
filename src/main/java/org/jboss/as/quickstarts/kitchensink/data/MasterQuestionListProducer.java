package org.jboss.as.quickstarts.kitchensink.data;

import org.jboss.as.quickstarts.kitchensink.model.MasterQuestion;

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
public class MasterQuestionListProducer {
   @Inject
   private EntityManager em;

   private List<MasterQuestion> masterQuestions;

   // @Named provides access the return value via the EL variable name "masterQuestions" in the UI (e.g.,
   // Facelets or JSP view)
   @Produces
   @Named
   public List<MasterQuestion> getMasterQuestions() {
      return masterQuestions;
   }

   public void onMasterQuestionListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final MasterQuestion question) {
      retrieveAllMasterQuestionsOrderedByName();
   }

   @PostConstruct
   public void retrieveAllMasterQuestionsOrderedByName() {
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery<MasterQuestion> criteria = cb.createQuery(MasterQuestion.class);
      Root<MasterQuestion> question = criteria.from(MasterQuestion.class);
      // Swap criteria statements if you would like to try out type-safe criteria queries, a new
      // feature in JPA 2.0
      // criteria.select(question).orderBy(cb.asc(question.get(Question_.name)));
      criteria.select(question).orderBy(cb.asc(question.get("question")));
      masterQuestions = em.createQuery(criteria).getResultList();
   }
}
