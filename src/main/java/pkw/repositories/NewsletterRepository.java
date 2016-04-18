package pkw.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pkw.models.Newsletter;

import java.util.List;

@Repository
public interface NewsletterRepository extends CrudRepository<Newsletter, Integer> {
}
