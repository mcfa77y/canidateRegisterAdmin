package org.jboss.as.quickstarts.kitchensink.data;

import org.jboss.as.quickstarts.kitchensink.model.Question;

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
public class QuestionListProducer {
   @Inject
   private EntityManager em;

   private List<Question> questions;

   // @Named provides access the return value via the EL variable name "questions" in the UI (e.g.,
   // Facelets or JSP view)
   @Produces
   @Named
   public List<Question> getQuestions() {
      return questions;
   }

   public void onQuestionListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Question question) {
      retrieveAllQuestionsOrderedByName();
   }

   @PostConstruct
   public void retrieveAllQuestionsOrderedByName() {
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery<Question> criteria = cb.createQuery(Question.class);
      Root<Question> question = criteria.from(Question.class);
      // Swap criteria statements if you would like to try out type-safe criteria queries, a new
      // feature in JPA 2.0
      // criteria.select(question).orderBy(cb.asc(question.get(Question_.name)));
      criteria.select(question).orderBy(cb.asc(question.get("question")));
      questions = em.createQuery(criteria).getResultList();
   }
}
