package com.itRoad.documents_service.services;

import com.itRoad.documents_service.models.Document;
import com.itRoad.documents_service.repositories.DocumentRepository;
import com.itRoad.documents_service.exceptions.DocumentAlreadyExistsException;
import com.itRoad.documents_service.exceptions.DocumentNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    // Create a new document
    public Document createDocument(Document document) {
        // Check if document name already exists for this user
        if (documentRepository.existsByNameAndUserId(document.getName(), document.getUserId())) {
            throw new DocumentAlreadyExistsException("Document with name '" + document.getName() + "' already exists for this user");
        }

        // Set current date if not provided
        if (document.getDate() == null) {
            document.setDate(LocalDate.now());
        }

        return documentRepository.save(document);
    }

    // Get all documents
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    // Get a document by its ID
    public Document getDocumentById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found with id: " + id));
    }

    // Get documents by user ID
    public List<Document> getDocumentsByUserId(Long userId) {
        return documentRepository.findByUserId(userId);
    }

    // Get documents by type
    public List<Document> getDocumentsByType(String type) {
        return documentRepository.findByType(type);
    }

    // Get documents by category
    public List<Document> getDocumentsByCategory(String category) {
        return documentRepository.findByCategory(category);
    }

    // Get documents by name
    public List<Document> getDocumentsByName(String name) {
        return documentRepository.findByName(name);
    }

    // Get documents by exact date
    public List<Document> getDocumentsByDate(LocalDate date) {
        return documentRepository.findByDate(date);
    }

    // Get documents in a date range
    public List<Document> getDocumentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return documentRepository.findByDateBetween(startDate, endDate);
    }

    // Get documents by user ID and type
    public List<Document> getDocumentsByUserAndType(Long userId, String type) {
        return documentRepository.findByUserIdAndType(userId, type);
    }

    // Get documents by user ID and category
    public List<Document> getDocumentsByUserAndCategory(Long userId, String category) {
        return documentRepository.findByUserIdAndCategory(userId, category);
    }

    // Get documents by user ID and date range
    public List<Document> getDocumentsByUserAndDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return documentRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
    }

    // Delete a document by ID
    public void deleteDocument(Long id) {
        if (!documentRepository.existsById(id)) {
            throw new DocumentNotFoundException("Document not found with id: " + id);
        }
        documentRepository.deleteById(id);
    }

    // Delete all documents for a user
    public void deleteDocumentsByUserId(Long userId) {
        List<Document> documents = documentRepository.findByUserId(userId);
        documentRepository.deleteAll(documents);
    }

    // Search documents by name, category, or type
    public List<Document> searchDocuments(String searchTerm) {
        return documentRepository.searchDocuments(searchTerm);
    }

    // Search documents by user and search term
    public List<Document> searchDocumentsByUser(Long userId, String searchTerm) {
        return documentRepository.searchDocumentsByUser(userId, searchTerm);
    }

    // Count documents for a user
    public Long countDocumentsByUser(Long userId) {
        return documentRepository.countDocumentsByUser(userId);
    }

    // Count documents by type
    public Long countDocumentsByType(String type) {
        return documentRepository.countDocumentsByType(type);
    }

    // Count documents by category
    public Long countDocumentsByCategory(String category) {
        return documentRepository.countDocumentsByCategory(category);
    }

    // Check if document exists by ID
    public boolean existsById(Long id) {
        return documentRepository.existsById(id);
    }

    // Check if document exists by name and user ID
    public boolean existsByNameAndUserId(String name, Long userId) {
        return documentRepository.existsByNameAndUserId(name, userId);
    }
}
