package com.sarapis.orservice.controller;

import com.sarapis.orservice.dto.ContactDTO;
import com.sarapis.orservice.dto.PaginationDTO;
import com.sarapis.orservice.exception.InvalidInputException;
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
    public ResponseEntity<PaginationDTO<ContactDTO>> getAllContacts(@RequestParam(defaultValue = "1") Integer page,
                                                                    @RequestParam(defaultValue = "10") Integer perPage) {
        List<ContactDTO> contactDTOs = this.contactService.getAllContacts();

        if(page <= 0) throw new InvalidInputException("Invalid value provided for 'page'.");
        if(perPage <= 0) throw new InvalidInputException("Invalid value provided for 'perPage'.");

        PaginationDTO<ContactDTO> paginationDTO = PaginationDTO.of(
            contactDTOs,
            page,
            perPage
        );
        return ResponseEntity.ok(paginationDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> getContactById(@PathVariable String id) {
        ContactDTO contactDTO = contactService.getContactById(id);
        return ResponseEntity.ok(contactDTO);
    }

    @PostMapping
    public ResponseEntity<ContactDTO> createContact(@RequestBody ContactDTO contactDTO) {
        ContactDTO createdContact = contactService.createContact(contactDTO);
        return ResponseEntity.ok(createdContact);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactDTO> updateContact(@PathVariable String id, @RequestBody ContactDTO contactDTO) {
        ContactDTO updatedContact = contactService.updateContact(id, contactDTO);
        return ResponseEntity.ok(updatedContact);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable String id) {
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }
}
