package com.sarapis.orservice.entity;

import com.sarapis.orservice.dto.ContactDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contact")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contact {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "title")
    private String title;

    @Column(name = "department")
    private String department;

    @Column(name = "email")
    private String email;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "contact_id")
    private List<Phone> phones = new ArrayList<>();

    public ContactDTO toDTO() {
        return ContactDTO.builder()
                .id(this.id)
                .name(this.name)
                .title(this.title)
                .department(this.department)
                .email(this.email)
                .phones(this.phones.stream().map(Phone::toDTO).toList())
                .attributes(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();
    }
}
