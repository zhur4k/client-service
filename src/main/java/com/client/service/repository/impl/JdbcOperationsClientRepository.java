package com.client.service.repository.impl;

import com.client.service.model.Client;
import com.client.service.repository.ClientRepository;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JdbcOperationsClientRepository implements ClientRepository, RowMapper<Client> {
    private final JdbcOperations jdbcOperations;

    public JdbcOperationsClientRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public List<Client> findAll() {

        return jdbcOperations.query("select * from t_client", this);
    }

    @Override
    public void save(Client client) {
        this.jdbcOperations.update("""
            insert into t_client(id, c_name, c_email, c_phone, c_created_at, c_updated_at) values (?, ?, ?, ?, ?, ?)
            """,new Object[]{
                client.id(),
                client.name(),
                client.email(),
                client.phone(),
                client.createdAt(),
                client.updatedAt()});
    }

    @Override
    public Optional<Client> findById(UUID id) {
        return jdbcOperations.query("select * from t_client where id = ?", new Object[]{id},this)
                .stream().findFirst();

    }

    @Override
    public void delete(UUID id) {
        jdbcOperations.update("delete from t_task where c_client_id = ?", id);
        jdbcOperations.update("delete from t_client where id = ?", id);
    }

    @Override
    public void update(Client client) {
        this.jdbcOperations.update("""
        update t_client set c_name = ?, c_email = ?, c_phone = ?, c_updated_at = ? where id = ?
        """,
                client.name(),
                client.email(),
                client.phone(),
                client.updatedAt(),
                client.id());
    }

    @Override
    public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Client(rs.getObject("id",UUID.class),
                rs.getObject("c_name",String.class),
                rs.getObject("c_email",String.class),
                rs.getObject("c_phone",String.class),
                rs.getObject("c_created_at", LocalDateTime.class),
                rs.getObject("c_updated_at", LocalDateTime.class));

    }
}
