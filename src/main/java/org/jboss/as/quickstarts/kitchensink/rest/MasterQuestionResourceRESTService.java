package org.jboss.as.quickstarts.kitchensink.rest;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jboss.as.quickstarts.kitchensink.model.MasterQuestion;
import org.jboss.as.quickstarts.kitchensink.model.Question;

/**
 * JAX-RS Example
 * 
 * This class produces a RESTful service to read the contents of the questions
 * table.
 */
@Path("/questions")
@RequestScoped
public class MasterQuestionResourceRESTService {
	@PersistenceContext
	@Inject
	private EntityManager em;

	public MasterQuestionResourceRESTService(EntityManager em2) {
		if (em == null)
			em = em2;
	}

	@GET
	@Produces("text/xml")
	public List<MasterQuestion> listAllMasterQuestions()
			throws NullPointerException {
		// Use @SupressWarnings to force IDE to ignore warnings about
		// "genericizing" the results of
		// this query
		@SuppressWarnings("unchecked")
		// We recommend centralizing inline queries such as this one into
		// @NamedQuery annotations on
		// the @Entity class
		// as described in the named query blueprint:
		// https://blueprints.dev.java.net/bpcatalog/ee5/persistence/namedquery.html
		final List<MasterQuestion> results = em.createQuery(
				"select q from MasterQuestion q order by q.question")
				.getResultList();
		return results;
	}

	@GET
	@Produces("text/xml")
	public List<Question> listAllQuestions() throws NullPointerException {
		// Use @SupressWarnings to force IDE to ignore warnings about
		// "genericizing" the results of
		// this query
		List<Question> questionList = new ArrayList<Question>();
		// return questionList;
		@SuppressWarnings("unchecked")
		List<MasterQuestion> masterQuestionList = listAllMasterQuestions();
		// convert MasterQuestions to normal questions and return them as a list
		for (MasterQuestion masterQuestion : masterQuestionList) {
			Question question = new Question();
			question.setQuestion(masterQuestion.getQuestion());
			questionList.add(question);
		}
		return new ArrayList<Question>(questionList);
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("text/xml")
	public MasterQuestion lookupQuestionById(@PathParam("id") long id) {
		return em.find(MasterQuestion.class, id);
	}
}
