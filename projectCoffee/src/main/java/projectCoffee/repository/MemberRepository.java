package projectCoffee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projectCoffee.entity.Member;

import javax.persistence.EntityManager;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);
}
