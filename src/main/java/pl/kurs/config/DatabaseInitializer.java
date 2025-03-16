package pl.kurs.config;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer {
    private final EntityManager entityManager;
    private final PlatformTransactionManager transactionManager;

    @PostConstruct
    public void init() {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.executeWithoutResult(status -> {
            entityManager.createNativeQuery("""
                    DROP VIEW IF EXISTS figure_max_area;
                    CREATE VIEW figure_max_area AS
                    SELECT id, 'circle' AS typ, pi() * radius * radius AS area FROM circle
                    UNION ALL
                    SELECT id, 'square' AS typ, side * side AS area FROM square
                    UNION ALL
                    SELECT id, 'rectangle' AS typ, width * height AS area FROM rectangle
                    ORDER BY area DESC
                    LIMIT 1;
                    """).executeUpdate();
        });
    }
}
