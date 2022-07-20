package life.genny.datagenerator.service;

import life.genny.datagenerator.repositories.ContactRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ContactService {

    @Inject
    ContactRepository contactRepository;



}
