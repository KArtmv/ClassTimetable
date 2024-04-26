package ua.foxminded.WebProject.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
@Slf4j
public class SqlScriptExecutor {

    private static final String SCRIPT_FILE = "/sql/data.sql";

    private final DataSource dataSource;

    @Transactional
    public boolean execute() {
        try {
            Connection connection = dataSource.getConnection();
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(SCRIPT_FILE));
            return true;
        } catch (SQLException e) {
            log.error("Executing sql script: {}, is failed: {}", SCRIPT_FILE, e.getMessage());
            return false;
        }
    }
}
