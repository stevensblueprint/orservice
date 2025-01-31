package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.ContactDTO;
import com.sarapis.orservice.dto.upsert.UpsertContactDTO;
import com.sarapis.orservice.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {
    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public ResponseEntity<List<ContactDTO>> getAllContacts() {
        List<ContactDTO> contacts = this.contactService.getAllContacts();
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/{contactId}")
    public ResponseEntity<ContactDTO> getContactById(@PathVariable String contactId) {
        ContactDTO contact = this.contactService.getContactById(contactId);
        return ResponseEntity.ok(contact);
    }

    @PostMapping
    public ResponseEntity<ContactDTO> createContact(@RequestBody UpsertContactDTO upsertContactDTO) {
        ContactDTO createdContact = this.contactService.createContact(upsertContactDTO);
        return ResponseEntity.ok(createdContact);
    }

    @PatchMapping("/{contactId}")
    public ResponseEntity<ContactDTO> updateContact(@PathVariable String contactId,
                                                    @RequestBody UpsertContactDTO upsertContactDTO) {
        ContactDTO updatedContact = this.contactService.updateContact(contactId, upsertContactDTO);
        return ResponseEntity.ok(updatedContact);
    }

    @DeleteMapping("/{contactId}")
    public ResponseEntity<Void> deleteContact(@PathVariable String contactId) {
        this.contactService.deleteContact(contactId);
        return ResponseEntity.noContent().build();
    }
}
