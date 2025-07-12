package com.itRoad.documents_service.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Document name - must be unique per user
    @NotBlank(message = "Document name is required")
    @Column(nullable = false)
    private String name;

     // Document type (e.g., PDF, DOCX, JPG, etc.)
    private String type;

    // Document category
    @NotBlank(message = "Document category is required")
    @Column(nullable = false)
    private String category;

    //Date when the document was created
    @Column(nullable = false)
    private LocalDate date;


    //URL or file path to the document
    private String url;

    // ID of the user who owns this document
    @NotNull(message = "User ID is required")
    @Column(nullable = false)
    private Long userId;
}
