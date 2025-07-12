package com.itRoad.documents_service.controllers;

import com.itRoad.documents_service.dto.ApiResponse;
import com.itRoad.documents_service.dto.UserDto;
import com.itRoad.documents_service.models.Document;
import com.itRoad.documents_service.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "*")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    // Get the currently authenticated user from the security context
    private UserDto getCurrentUser() {
        return (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    // Create a new document
    @PostMapping
    public ResponseEntity<ApiResponse<Document>> createDocument(@Valid @RequestBody Document document) {
        try {
            Document created = documentService.createDocument(document);
            return ResponseEntity.ok(new ApiResponse<>(true, "Document created", created));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Get all documents for a specific user by userId
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<List<Document>>> getDocumentsByUserId(@PathVariable Long userId) {
        try {
            List<Document> docs = documentService.getDocumentsByUserId(userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Documents retrieved", docs));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Get a document by its ID
    @GetMapping("/by-id/{id}")
    public ResponseEntity<ApiResponse<Document>> getDocumentById(@PathVariable Long id) {
        try {
            Document document = documentService.getDocumentById(id);
            if (document == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Document not found", null));
            }
            return ResponseEntity.ok(new ApiResponse<>(true, "Document found", document));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Delete a document by its ID
    @DeleteMapping("/by-id/{id}")
    public ResponseEntity<ApiResponse<String>> deleteDocument(@PathVariable Long id) {
        try {
            Document document = documentService.getDocumentById(id);
            if (document == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Document not found", null));
            }
            documentService.deleteDocument(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Document deleted", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Search documents of a user based on a keyword
    @GetMapping("/user/{userId}/search")
    public ResponseEntity<ApiResponse<List<Document>>> searchDocumentsByUser(
            @PathVariable Long userId, @RequestParam String term) {
        try {
            List<Document> docs = documentService.searchDocumentsByUser(userId, term);
            return ResponseEntity.ok(new ApiResponse<>(true, "Search completed", docs));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Count how many documents a user has
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<ApiResponse<Long>> countDocumentsByUser(@PathVariable Long userId) {
        try {
            Long count = documentService.countDocumentsByUser(userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Count retrieved", count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), 0L));
        }
    }
}
