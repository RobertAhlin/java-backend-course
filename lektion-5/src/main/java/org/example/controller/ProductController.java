package org.example.controller;

import org.example.exception.ErrorResponse;
import org.example.handler.GlobalExceptionHandler;
import org.example.http.Response;
import org.example.model.Product;
import org.example.service.ProductService;

public class ProductController {
    private final ProductService productService;
    private final GlobalExceptionHandler exceptionHandler;

    public ProductController(ProductService productService, GlobalExceptionHandler exceptionHandler) {
        this.productService = productService;
        this.exceptionHandler = exceptionHandler;
    }

    public Response getProduct(Long id, String requestPath) {
        try {
            Product product = productService.getProduct(id);
            return Response.ok(product);
        } catch (Exception e) {
            ErrorResponse errorResponse = exceptionHandler.handleException(e, requestPath);
            return Response.error(errorResponse);
        }
    }
}
