package com.dev.service;

import com.dev.enumm.DeliveryStatus;
import com.dev.enumm.OrderStatus;
import com.dev.exception.NoDeliveryAgentAvailableException;
import com.dev.exception.OrderNotFoundException;
import com.dev.model.DeliveryGuy;
import com.dev.model.Order;
import com.dev.repository.DeliveryGuyRepository;
import com.dev.repository.OrderRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class OrderService {

    private final OrderRepository repository;
    private final DeliveryGuyRepository deliveryGuyRepository;
    private final Random random = new Random();

    public OrderService(OrderRepository repository, DeliveryGuyRepository deliveryGuyRepository) {
        this.repository = repository;
        this.deliveryGuyRepository = deliveryGuyRepository;
    }

    public List<Order> getActiveAdminOrders() {
        return repository.findAll().stream()
                .filter(o -> o.getStatus() != OrderStatus.DELIVERED && o.getStatus() != OrderStatus.CANCELLED)
                .collect(Collectors.toList());
    }

    public Optional<Order> getOrderById(int orderId) {
        return repository.findById(orderId);
    }

    public List<Order> getOrdersByCustomer(String username) {
        return repository.findAll().stream()
                .filter(o -> o.getCustomerUsername().equalsIgnoreCase(username))
                .collect(Collectors.toList());
    }

    public Order placeOrder(String customerUsername, Map<Integer, Integer> orderedItems,
                            double total, double discount, String paymentMethod) {
        Order order = new Order(repository.nextId(), customerUsername, orderedItems, total, discount,
                OrderStatus.PLACED, paymentMethod);
        return repository.save(order);
    }

    public boolean updateOrderStatus(int orderId, OrderStatus newStatus) {
        Optional<Order> orderOpt = repository.findById(orderId);
        if (orderOpt.isEmpty()) {
            return false;
        }
        Order order = orderOpt.get();

        if (newStatus == OrderStatus.READY_FOR_DELIVERY) {
            boolean assigned = tryAssignDeliveryGuy(order);
            if (!assigned) {
                throw new NoDeliveryAgentAvailableException(
                        "No delivery agents are available right now.");
            }
        }

        order.setStatus(newStatus);
        repository.save(order);
        return true;
    }

    private boolean tryAssignDeliveryGuy(Order order) {
        List<DeliveryGuy> available = deliveryGuyRepository.findAll().stream()
                .filter(g -> g.getStatus() == DeliveryStatus.AVAILABLE)
                .collect(Collectors.toList());

        if (available.isEmpty()) {
            return false;
        }

        DeliveryGuy chosen = available.get(random.nextInt(available.size()));
        chosen.setStatus(DeliveryStatus.BUSY);
        deliveryGuyRepository.save(chosen);

        order.setAssignedDeliveryGuyUsername(chosen.getUsername());
        System.out.println("Order #" + order.getOrderId() + " auto-assigned to agent " + chosen.getUsername());
        return true;
    }

    public List<Order> getOrdersForAgent(String username, OrderStatus status)
    {
        return getActiveAdminOrders().stream()
                .filter(o -> o.getStatus() == status
                        && username.equalsIgnoreCase(o.getAssignedDeliveryGuyUsername()))
                .collect(Collectors.toList());
    }

    public boolean pickupOrder(int orderId, String agentUsername)
    {
        boolean ownsOrder = getOrdersForAgent(agentUsername, OrderStatus.READY_FOR_DELIVERY).stream()
                .anyMatch(o -> o.getOrderId() == orderId);
        if (!ownsOrder) {
            throw new OrderNotFoundException("This order is not assigned to you or is not ready.");
        }
        return updateOrderStatus(orderId, OrderStatus.OUT_FOR_DELIVERY);
    }

    public boolean deliverOrder(int orderId, DeliveryGuy deliveryGuy)
    {
        boolean ownsOrder = getOrdersForAgent(deliveryGuy.getUsername(), OrderStatus.OUT_FOR_DELIVERY).stream()
                .anyMatch(o -> o.getOrderId() == orderId);
        if (!ownsOrder) {
            throw new OrderNotFoundException("This order is not assigned to you or is not out for delivery.");
        }

        boolean updated = updateOrderStatus(orderId, OrderStatus.DELIVERED);
        if (updated) {
            deliveryGuyRepository.findByUsername(deliveryGuy.getUsername())
                    .ifPresent(g -> g.setStatus(DeliveryStatus.AVAILABLE));
            deliveryGuy.setStatus(DeliveryStatus.AVAILABLE);
        }
        return updated;
    }
}