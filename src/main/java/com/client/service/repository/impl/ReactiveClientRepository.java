package com.client.service.repository.impl;

import com.client.service.model.Client;
import com.client.service.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ReactiveClientRepository implements ClientRepository {

    private final DatabaseClient databaseClient;

    @Override
    public Flux<Client> findAll() {
        return databaseClient.sql("SELECT * FROM t_client")
                .map((row, metadata) -> new Client(
                        row.get("id", UUID.class),
                        row.get("c_name", String.class),
                        row.get("c_email", String.class),
                        row.get("c_phone", String.class),
                        row.get("c_created_at", LocalDateTime.class),
                        row.get("c_updated_at", LocalDateTime.class)))
                .all();
    }
    @Override
    public Mono<Client> findById(Mono<UUID> idMono) {
        return idMono.flatMap(id ->
                databaseClient.sql("SELECT * FROM t_client WHERE id = :id")
                        .bind("id", id)
                        .map((row, metadata) -> new Client(
                                row.get("id", UUID.class),
                                row.get("c_name", String.class),
                                row.get("c_email", String.class),
                                row.get("c_phone", String.class),
                                row.get("c_created_at", LocalDateTime.class),
                                row.get("c_updated_at", LocalDateTime.class)))
                        .one()
        );
    }
    @Override
    public Mono<Void> save(Mono<Client> clientMono) {
        return clientMono.flatMap(client -> databaseClient.sql("""
            INSERT INTO t_client (id, c_name, c_email, c_phone, c_created_at, c_updated_at) 
            VALUES (:id, :name, :email, :phone, :createdAt, :updatedAt)
            """)
                .bind("id", client.id())
                .bind("name", client.name())
                .bind("email", client.email())
                .bind("phone", client.phone())
                .bind("createdAt", client.createdAt())
                .bind("updatedAt", client.updatedAt())
                .fetch()
                .rowsUpdated()
                .then());
    }
    @Override
    public Mono<Void> delete(Mono<UUID> idMono) {
        return idMono.flatMap(id -> databaseClient.sql("DELETE FROM t_client WHERE id = :id")
                .bind("id", id)
                .fetch()
                .rowsUpdated()
                .then());
    }
    @Override
    public Mono<Void> update(Mono<Client> clientMono) {
        return clientMono.flatMap(client -> databaseClient.sql("""
            UPDATE t_client 
            SET c_name = :name, c_email = :email, c_phone = :phone, c_updated_at = :updatedAt 
            WHERE id = :id
            """)
                .bind("id", client.id())
                .bind("name", client.name())
                .bind("email", client.email())
                .bind("phone", client.phone())
                .bind("updatedAt", client.updatedAt())
                .fetch()
                .rowsUpdated()
                .then());
    }
}
