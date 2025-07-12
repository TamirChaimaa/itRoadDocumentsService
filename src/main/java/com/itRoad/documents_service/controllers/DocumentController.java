package com.itRoad.documents_service.controllers;

import com.itRoad.documents_service.dto.ApiResponse;
import com.itRoad.documents_service.dto.UserDto;
import com.itRoad.documents_service.models.Document;
import com.itRoad.documents_service.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "*")
public class DocumentController {

    @Autowired
    private DocumentService documentService;
    private final Path uploadDir = Paths.get("uploads");

    // Get the currently authenticated user from the security context
    private UserDto getCurrentUser() {
        return (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    // Create a new document
    @PostMapping
    public ResponseEntity<?> uploadDocument(
            @RequestParam("title") String title,
            @RequestParam("category") String category,
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId,
            @RequestParam(value = "description", required = false) String description
    ) {
        try {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileName = UUID.randomUUID() + "_" + originalFilename;

            Path targetLocation = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Extraire le type de fichier
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toUpperCase();
            }

            Document doc = new Document();
            doc.setName(title); // Utiliser le titre fourni par l'utilisateur
            doc.setCategory(category);
            doc.setType(fileExtension); // Définir le type basé sur l'extension
            doc.setUserId(userId);
            doc.setUrl("/api/documents/download/" + fileName);

            Document savedDoc = documentService.createDocument(doc);

            // Retourner une réponse avec ApiResponse pour la cohérence
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Document created successfully", savedDoc));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "File upload failed: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error creating document: " + e.getMessage(), null));
        }
    }

    // Endpoint de téléchargement fichier (exemple)
    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<?> downloadFile(@PathVariable String filename) {
        try {
            Path filePath = uploadDir.resolve(filename).normalize();
            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            byte[] data = Files.readAllBytes(filePath);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                    .body(data);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Could not download file: " + e.getMessage(), null));
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