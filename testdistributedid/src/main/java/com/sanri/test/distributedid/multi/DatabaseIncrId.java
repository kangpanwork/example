package com.sanri.test.distributedid.multi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Objects;

@Component
public class DatabaseIncrId implements IdGenerator {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    String sql = "insert into SEQUENCE_ID(value) values ('1')";
    PreparedStatementCreatorFactory preparedStatementCreatorFactory = new PreparedStatementCreatorFactory(sql);
    @Override
    public String generateId(int bizType) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        preparedStatementCreatorFactory.setReturnGeneratedKeys(true);
        PreparedStatementCreator preparedStatementCreator = preparedStatementCreatorFactory.newPreparedStatementCreator(new ArrayList<>());
        jdbcTemplate.update(preparedStatementCreator,keyHolder);
        return Objects.toString(keyHolder.getKey());
    }
}
