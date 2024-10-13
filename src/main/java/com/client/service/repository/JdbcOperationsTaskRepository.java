package com.client.service.repository;

import com.client.service.model.Task;
import com.client.service.model.TaskStatus;
import com.client.service.repository.impl.TaskRepository;
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
public class JdbcOperationsTaskRepository implements TaskRepository, RowMapper<Task> {
    private final JdbcOperations jdbcOperations;

    public JdbcOperationsTaskRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public List<Task> findAll() {

        return jdbcOperations.query("select * from t_task", this);
    }

    @Override
    public void save(Task task) {
        this.jdbcOperations.update("""
            insert into t_task(id, c_user_id, c_title, c_description, status, c_created_at, c_updated_at) values (?, ?, ?, ?, ?, ?,?)
            """,new Object[]{
                task.id(),
                task.userId(),
                task.title(),
                task.description(),
                task.status(),
                task.createdAt(),
                task.updatedAt()
                });
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return jdbcOperations.query("select * from t_task where id = ?", new Object[]{id},this)
                .stream().findFirst();

    }

    @Override
    public void delete(UUID id) {
        jdbcOperations.update("delete from t_task where id = ?",id);
    }

    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Task(rs.getObject("id",UUID.class),
                rs.getObject("c_user_id",UUID.class),
                rs.getObject("c_title",String.class),
                rs.getObject("c_description",String.class),
                rs.getObject("c_status", TaskStatus.class),
                rs.getObject("c_created_at", LocalDateTime.class),
                rs.getObject("c_updated_at", LocalDateTime.class));

    }
}
