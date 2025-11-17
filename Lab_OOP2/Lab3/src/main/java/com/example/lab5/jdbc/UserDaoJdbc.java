package com.example.lab5.jdbc;

import com.example.lab5.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

public class UserDaoJdbc {
    private final DataSource ds;
    private final Logger log = LoggerFactory.getLogger(UserDaoJdbc.class);

    public UserDaoJdbc(DataSource ds){ this.ds = ds; }

    public Long create(User user) throws SQLException {
        String sql = "INSERT INTO users(username, email) VALUES (?, ?) RETURNING id";
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getLong(1);
            throw new SQLException("Insert failed");
        }
    }

    public Optional<User> findById(Long id) throws SQLException {
        String sql = "SELECT id, username, email, created_at FROM users WHERE id = ?";
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                User u = new User();
                u.setId(rs.getLong("id"));
                u.setUsername(rs.getString("username"));
                u.setEmail(rs.getString("email"));
                return Optional.of(u);
            }
            return Optional.empty();
        }
    }
}