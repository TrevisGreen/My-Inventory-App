package com.myInventory.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
// Defines a table at the class level
@Table
public class Item implements Serializable {

    // Will be the unique id generated for this object
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // To mange tow or more users editing the same thing
    @Version
    private Integer version;

    // The quantity of the item, initialized to 1
    private Integer quantity = 1;

    /* Price of the item, initialized to 0 and must not be empty
     * and it is formatted with two decimal places because it's money
     */
    @Column(nullable = false, length = 12, scale = 2, precision = 8)
    private BigDecimal price = BigDecimal.ZERO;

    // Many items to one invoice relationship
    @ManyToOne
    private Invoice invoice;

    // Many items to one product relationship
    @ManyToOne
    private Product product;

    // Default constructor
    public Item() {
    }

    // Getters and Setters generated by the IDE

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
