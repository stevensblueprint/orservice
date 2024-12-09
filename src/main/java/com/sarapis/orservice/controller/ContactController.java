package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.ContactDTO;
import com.sarapis.orservice.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
        List<ContactDTO> contactDTOs = this.contactService.getAllContacts();
        return ResponseEntity.ok(contactDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> getContactById(@PathVariable String id) {
        ContactDTO contactDTO = this.contactService.getContactById(id);
        return ResponseEntity.ok(contactDTO);
    }

    @PostMapping
    public ResponseEntity<ContactDTO> createContact(@RequestBody ContactDTO contactDTO) {
        if (contactDTO.getId() == null) {
            contactDTO.setId(UUID.randomUUID().toString());
        }
        ContactDTO createdContact = this.contactService.createContact(contactDTO);
        return ResponseEntity.ok(createdContact);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactDTO> updateContact(@PathVariable String id, @RequestBody ContactDTO contactDTO) {
        ContactDTO updatedAccessibility = this.contactService.updateContact(id, contactDTO);
        return ResponseEntity.ok(updatedAccessibility);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable String id) {
        this.contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }
}
