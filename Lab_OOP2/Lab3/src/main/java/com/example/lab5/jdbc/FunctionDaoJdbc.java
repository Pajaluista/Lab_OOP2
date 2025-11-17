package com.example.lab5.jdbc;

import com.example.lab5.entity.FunctionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FunctionDaoJdbc {
    private final DataSource ds;
    private final Logger log = LoggerFactory.getLogger(FunctionDaoJdbc.class);

    public FunctionDaoJdbc(DataSource ds){ this.ds = ds; }

    public Long create(FunctionEntity f) throws SQLException {
        String sql = "INSERT INTO functions(user_id, name, expression, description) VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setLong(1, f.getUserId());
            ps.setString(2, f.getName());
            ps.setString(3, f.getExpression());
            ps.setString(4, f.getDescription());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getLong(1);
            throw new SQLException("Insert failed");
        }
    }

    public Optional<FunctionEntity> findById(Long id) throws SQLException {
        String sql = "SELECT id, user_id, name, expression, description FROM functions WHERE id = ?";
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                FunctionEntity f = new FunctionEntity();
                f.setId(rs.getLong("id"));
                f.setUserId(rs.getLong("user_id"));
                f.setName(rs.getString("name"));
                f.setExpression(rs.getString("expression"));
                f.setDescription(rs.getString("description"));
                return Optional.of(f);
            }
            return Optional.empty();
        }
    }

    public List<FunctionEntity> findByUserId(Long userId) throws SQLException {
        String sql = "SELECT id, user_id, name, expression, description FROM functions WHERE user_id = ?";
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            List<FunctionEntity> res = new ArrayList<>();
            while (rs.next()){
                FunctionEntity f = new FunctionEntity();
                f.setId(rs.getLong("id"));
                f.setUserId(rs.getLong("user_id"));
                f.setName(rs.getString("name"));
                f.setExpression(rs.getString("expression"));
                f.setDescription(rs.getString("description"));
                res.add(f);
            }
            return res;
        }
    }
}