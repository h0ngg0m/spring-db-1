package hello.springdb1.jdbc.repository;

import hello.springdb1.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 repository = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {
        Member m = new Member("memberA", 10000);
        repository.save(m);

        Member findMember = repository.findById("memberA");
        log.info("findMember={}", findMember);
        Assertions.assertThat(findMember).isEqualTo(m);

        repository.update(m.getMemberId(), 20000);
        Member updatedMember = repository.findById("memberA");
        Assertions.assertThat(updatedMember.getMoney()).isEqualTo(20000);
    }

}