package com.fetcher.positions.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "Positions")
public class Position {

    private Integer id;
    private UUID externalId;
    private String name;
    private String currentCompany;
    private PositionType type;
    private String location;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name="external_id", nullable = false)
    public UUID getExternalId() {
        return externalId;
    }

    public void setExternalId(UUID externalId) {
        this.externalId = externalId;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "current_company")
    public String getCurrentCompany() {
        return currentCompany;
    }

    public void setCurrentCompany(String currentCompany) {
        this.currentCompany = currentCompany;
    }

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    public PositionType getType() {
        return type;
    }

    public void setType(PositionType type) {
        this.type = type;
    }

    @Column(name = "location")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
