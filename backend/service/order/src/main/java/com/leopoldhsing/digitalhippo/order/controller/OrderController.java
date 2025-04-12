package com.leopoldhsing.digitalhippo.order.controller;

import com.leopoldhsing.digitalhippo.model.dto.ErrorResponseDto;
import com.leopoldhsing.digitalhippo.model.entity.Order;
import com.leopoldhsing.digitalhippo.order.service.OrderService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "REST APIs for Order Management", description = "REST APIs for managing orders")
@RestController
@RequestMapping("/api/stripe/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(
            summary = "Get order details",
            description = "Retrieve order details by order ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Order not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(
            @Parameter(description = "Order ID", required = true, example = "1")
            @PathVariable Long orderId
    ) {
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }
}
