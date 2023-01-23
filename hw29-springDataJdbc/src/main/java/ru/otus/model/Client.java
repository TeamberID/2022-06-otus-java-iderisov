package ru.otus.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table(name = "client")
public class Client {

    @Id
    private final Long id;
    private final String name;

    @MappedCollection(idColumn = "client_id", keyColumn = "client_id")
    private final List<Phone> phones;

    @MappedCollection(idColumn = "client_id")
    private final Address address;

    public Client(String name) {
        this(null, name, new ArrayList<>(), null);
    }

    @PersistenceCreator
    public Client(Long id, String name, List<Phone> phones, Address address) {
        this.id = id;
        this.name = name;
        this.phones = phones;
        this.address = address;
    }

}
