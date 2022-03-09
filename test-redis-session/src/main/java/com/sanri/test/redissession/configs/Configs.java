package com.sanri.test.redissession.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.session.data.redis.SessionOperation;

@Configuration
@Import(SessionOperation.class)
public class Configs {
}
