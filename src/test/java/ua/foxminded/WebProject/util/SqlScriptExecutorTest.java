package ua.foxminded.WebProject.util;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SqlScriptExecutor.class}))
@ActiveProfiles("test")
@FlywayTest
class SqlScriptExecutorTest {

    @Autowired
    private SqlScriptExecutor scriptExecutor;

    @Test
    void execute_ShouldReturnTrue_WhenScriptExecutesSuccessfully() {
        assertTrue(scriptExecutor.execute());
    }

    @Test
    void execute_ShouldReturnFalse_WhenScriptExecutesFailed() throws SQLException {
        DataSource mockDataSource = mock(DataSource.class);
        scriptExecutor = mock(SqlScriptExecutor.class);

        when(mockDataSource.getConnection()).thenThrow(SQLException.class);

        assertFalse(scriptExecutor.execute());
    }
}