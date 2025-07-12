package com.itRoad.documents_service.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    // Indicates if the operation was successful
    private boolean success;

    // message describing the result
    private String message;

     //The actual data payload
    private T data;
}
