package com.example.lab5;

import org.junit.jupiter.api.Test;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import com.example.lab5.jdbc.UserDaoJdbc;
import com.example.lab5.entity.User;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcSmokeTest {
    @Test
    public void testCreateUser() throws Exception {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1");
        ds.setUser("sa");
        ds.setPassword("");
        // run create_tables.sql
        String sql = new String(Files.readAllBytes(Paths.get("resources/scripts/create_tables.sql")));
        try (Connection c = ds.getConnection(); Statement st = c.createStatement()){
            st.execute(sql);
        }
        UserDaoJdbc dao = new UserDaoJdbc(ds);
        User u = new User();
        u.setUsername("alice");
        u.setEmail("alice@example.com");
        Long id = dao.create(u);
        assertNotNull(id);
        assertTrue(dao.findById(id).isPresent());
    }
}