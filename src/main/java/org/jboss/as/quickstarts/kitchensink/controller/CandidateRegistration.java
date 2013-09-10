package org.jboss.as.quickstarts.kitchensink.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.as.quickstarts.kitchensink.model.Candidate;
import org.jboss.as.quickstarts.kitchensink.rest.MasterQuestionResourceRESTService;

// The @Stateful annotation eliminates the need for manual transaction demarcation
@Stateful
// The @Model stereotype is a convenience mechanism to make this a
// request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation
@Model
public class CandidateRegistration {

	@Inject
	private Logger log;

	@Inject
	private FacesContext facesContext;

	@Inject
	private EntityManager em;

	@Inject
	private Event<Candidate> candidateEventSrc;

	private Candidate newCandidate;

	@Produces
	@Named
	public Candidate getNewCandidate() {
		return newCandidate;
	}

	public void register() throws Exception {
		log.info("Registering " + newCandidate.getName());
		em.persist(newCandidate);
		facesContext.addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_INFO, "Registered!",
				"Registration successful"));
		candidateEventSrc.fire(newCandidate);
		initNewCandidate();
	}

	@PostConstruct
	public void initNewCandidate() {
		MasterQuestionResourceRESTService masterQuestionResourceRESTService = new MasterQuestionResourceRESTService(
				em);
		newCandidate = new Candidate();
		// init masterQuestions
		newCandidate.setQuestions(masterQuestionResourceRESTService
				.listAllQuestions());

	}
}
