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

import org.jboss.as.quickstarts.kitchensink.model.MasterQuestion;

// The @Stateful annotation eliminates the need for manual transaction demarcation
@Stateful
// The @Model stereotype is a convenience mechanism to make this a
// request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation
@Model
public class MasterQuestionRegistration {

	@Inject
	private Logger log;

	@Inject
	private FacesContext facesContext;

	@Inject
	private EntityManager em;

	@Inject
	private Event<MasterQuestion> questionEventSrc;

	private MasterQuestion newMasterQuestion;

	@Produces
	@Named
	public MasterQuestion getNewMasterQuestion() {
		return newMasterQuestion;
	}

	public void register() throws Exception {
		log.info("Registering " + newMasterQuestion.getQuestion());
		em.persist(newMasterQuestion);
		facesContext.addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_INFO, "Registered!",
				"Registration successful"));
		questionEventSrc.fire(newMasterQuestion);
		initNewMasterQuestion();
	}

	public void remove(MasterQuestion masterQuestion) throws Exception {
		log.info("Removing " + masterQuestion.getQuestion());
		MasterQuestion mq = em.getReference(MasterQuestion.class,
				masterQuestion.getId());
		em.remove(mq);
		facesContext.addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_INFO, "Removed!", "Removal successful"));
		questionEventSrc.fire(masterQuestion);
	}

	@PostConstruct
	public void initNewMasterQuestion() {
		newMasterQuestion = new MasterQuestion();
	}
}
