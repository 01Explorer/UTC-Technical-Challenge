package com.challenge.tobacco.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.Instant;

@Entity
@Table(name = "bundles", schema = "tobacco")
public class Bundle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "label", length = Integer.MAX_VALUE)
    private String label;

    @Column(name = "bought_at")
    private Instant boughtAt;

    @ManyToOne
    @JoinColumn(name = "producer_id")
    private Producer producer;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private TobaccoClass classField;

    @Column(name = "weight")
    private Double weight;

    protected Bundle() {}

    public Bundle(String label, Instant boughtAt, Producer producer, TobaccoClass classField, Double weight) {
        Assert.isTrue(boughtAt.isBefore(Instant.now().plusSeconds(20)), "Bought at can't be after now");
        Assert.isTrue(StringUtils.hasText(label), "Label can't be empty");
        Assert.notNull(producer, "Producer can't be null");
        Assert.notNull(classField, "Class can't be null");
        Assert.isTrue(weight > 0, "Weight needs to be greater than 0");
        this.label = label;
        this.boughtAt = boughtAt;
        this.producer = producer;
        this.classField = classField;
        this.weight = weight;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Instant getBoughtAt() {
        return boughtAt;
    }

    public void setBoughtAt(Instant boughtAt) {
        this.boughtAt = boughtAt;
    }

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }

    public TobaccoClass getClassField() {
        return classField;
    }

    public void setClassField(TobaccoClass classField) {
        this.classField = classField;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

}