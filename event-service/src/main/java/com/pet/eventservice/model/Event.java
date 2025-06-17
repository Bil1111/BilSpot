package com.pet.eventservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name="events")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name must not be empty")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @Future(message = "Date must be in the future.")
    private LocalDate date;

    @NotEmpty(message = "Venue must not be empty")
    @Size(min = 3, max = 150, message = "Venue must be between 3 and 150 characters")
    private String venue;

    @NotEmpty(message = "Artist must not be empty")
    @Size(min = 3, max = 100, message = "Artist name must be between 3 and 100 characters")
    private String artist;

    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    private String description;
    @Column(columnDefinition = "VARCHAR(255)")
    @NotNull(message = "File")
    private String imageURL;
//    @JsonManagedReference
//    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Ticket> tickets;

}
