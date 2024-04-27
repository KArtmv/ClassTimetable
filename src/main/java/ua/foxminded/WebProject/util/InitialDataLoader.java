package ua.foxminded.WebProject.util;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Component
@RequiredArgsConstructor
public class InitialDataLoader {

    private static final String SCRIPT_FILE = "/sql/data.sql";

    private final DataSource dataSource;

    @Transactional
    public void populate() {
        Resource resource = new ClassPathResource(SCRIPT_FILE);
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(resource);
        databasePopulator.execute(dataSource);
    }
}
