package hello.springdb1.jdbc.repository;

import com.zaxxer.hikari.HikariDataSource;
import hello.springdb1.jdbc.connection.ConnectionConst;
import hello.springdb1.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MemberRepositoryV1Test {

    MemberRepositoryV1 repository;

    @BeforeEach
    void setUp() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource(ConnectionConst.URL, ConnectionConst.USERNAME, ConnectionConst.PASSWORD);

        // 커넥션 풀링
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(ConnectionConst.URL);
        dataSource.setUsername(ConnectionConst.USERNAME);
        dataSource.setPassword(ConnectionConst.PASSWORD);

        repository = new MemberRepositoryV1(dataSource);
    }

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

        // delete
        repository.delete("memberA");
        Assertions.assertThatThrownBy(() -> repository.findById("memberA"))
                .isInstanceOf(NoSuchElementException.class);
    }

}