package life.genny.datagenerator.service;

import life.genny.datagenerator.repository.ContactRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ContactService {

    @Inject
    ContactRepository contactRepository;
}
