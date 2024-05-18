package hello.springdb1.jdbc.repository;

import hello.springdb1.jdbc.connection.DBConnectionUtil;
import hello.springdb1.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;

@Slf4j
public class MemberRepositoryV0 {

    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values(?, ?)";
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate();
            return member;
        } catch (Exception e) {
            log.error("getConnection error", e);
            throw e;
        } finally {
            close(connection, pstmt, null);
        }
    }

    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, memberId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Member(rs.getString("member_id"), rs.getInt("money"));
            } else {
                throw new NoSuchElementException("member not found, memberId=" + memberId);
            }
        } catch (Exception e) {
            log.error("getConnection error", e);
            throw e;
        } finally {
            close(connection, pstmt, rs);
        }
    }

    public void update(String memberId, int money) throws SQLException {
        String sql = "update member set money = ? where member_id = ?";
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("update resultSize={}", resultSize);
        } catch (Exception e) {
            log.error("getConnection error", e);
            throw e;
        } finally {
            close(connection, pstmt, null);
        }
    }

    private void close(Connection connection, Statement stmt, ResultSet rs) {
        if (rs != null)
            try {
                rs.close();
            } catch (SQLException e) {
                log.error("rs close error", e);
            }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.error("pstmt close error", e);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("connection close error", e);
            }
        }
    }

    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}
