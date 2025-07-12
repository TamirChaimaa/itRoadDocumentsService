package com.itRoad.documents_service.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentStatistics {

     //Total number of documents in the system
    private Long totalDocuments;

    //Number of documents created this month
    private Long documentsThisMonth;

     //Number of documents created this year
    private Long documentsThisYear;
}

