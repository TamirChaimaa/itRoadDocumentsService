package com.itRoad.documents_service.repositories;

import com.itRoad.documents_service.models.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {


    /**
     * Find all documents belonging to a specific user
     * @param userId The ID of the user
     * @return List of documents owned by the user
     */
    List<Document> findByUserId(Long userId);

    /**
     * Find all documents of a specific type
     * @param type The document type (e.g., PDF, DOCX)
     * @return List of documents of the specified type
     */
    List<Document> findByType(String type);

    /**
     * Find all documents in a specific category
     * @param category The document category
     * @return List of documents in the specified category
     */
    List<Document> findByCategory(String category);

    /**
     * Find documents by exact name match
     * @param name The document name
     * @return List of documents with the specified name
     */
    List<Document> findByName(String name);

    /**
     * Find documents created on a specific date
     * @param date The creation date
     * @return List of documents created on the specified date
     */
    List<Document> findByDate(LocalDate date);


    /**
     * Find documents within a date range
     * @param startDate The start date (inclusive)
     * @param endDate The end date (inclusive)
     * @return List of documents within the date range
     */
    List<Document> findByDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Find documents by user and type
     * @param userId The user ID
     * @param type The document type
     * @return List of documents matching both criteria
     */
    List<Document> findByUserIdAndType(Long userId, String type);

    /**
     * Find documents by user and category
     * @param userId The user ID
     * @param category The document category
     * @return List of documents matching both criteria
     */
    List<Document> findByUserIdAndCategory(Long userId, String category);

    /**
     * Find documents by user within a date range
     * @param userId The user ID
     * @param startDate The start date (inclusive)
     * @param endDate The end date (inclusive)
     * @return List of documents matching all criteria
     */
    List<Document> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    // =============================================================================
    // EXISTENCE CHECKS
    // =============================================================================

    /**
     * Check if a document with the given name exists for a specific user
     * @param name The document name
     * @param userId The user ID
     * @return true if document exists, false otherwise
     */
    boolean existsByNameAndUserId(String name, Long userId);

    // =============================================================================
    // CUSTOM QUERY METHODS
    // =============================================================================

    /**
     * Search documents by name, category, or type using partial matching
     * @param searchTerm The search term
     * @return List of documents matching the search criteria
     */
    @Query("SELECT d FROM Document d WHERE d.name LIKE %:searchTerm% OR d.category LIKE %:searchTerm% OR d.type LIKE %:searchTerm%")
    List<Document> searchDocuments(@Param("searchTerm") String searchTerm);

    /**
     * Search documents for a specific user by name, category, or type
     * @param userId The user ID
     * @param searchTerm The search term
     * @return List of documents matching the search criteria for the user
     */
    @Query("SELECT d FROM Document d WHERE d.userId = :userId AND (d.name LIKE %:searchTerm% OR d.category LIKE %:searchTerm% OR d.type LIKE %:searchTerm%)")
    List<Document> searchDocumentsByUser(@Param("userId") Long userId, @Param("searchTerm") String searchTerm);

    // =============================================================================
    // COUNTING METHODS
    // =============================================================================

    /**
     * Count total documents for a specific user
     * @param userId The user ID
     * @return Number of documents owned by the user
     */
    @Query("SELECT COUNT(d) FROM Document d WHERE d.userId = :userId")
    Long countDocumentsByUser(@Param("userId") Long userId);

    /**
     * Count documents of a specific type
     * @param type The document type
     * @return Number of documents of the specified type
     */
    @Query("SELECT COUNT(d) FROM Document d WHERE d.type = :type")
    Long countDocumentsByType(@Param("type") String type);

    /**
     * Count documents in a specific category
     * @param category The document category
     * @return Number of documents in the specified category
     */
    @Query("SELECT COUNT(d) FROM Document d WHERE d.category = :category")
    Long countDocumentsByCategory(@Param("category") String category);
}