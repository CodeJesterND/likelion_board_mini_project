package techit.boardmvc.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import techit.boardmvc.domain.Board;

import java.util.List;

@RequiredArgsConstructor
public class PaginationAndSortingRepositoryImpl implements PaginationAndSortingRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Board> getSortedBoardPage(int page, int size) {
        int beginPoint = (page - 1) * size; // 페이지 값 계산
        int FinalPoint = size;
        String sql = "SELECT id, name, title, password, content, created_at, updated_at FROM board ORDER BY id DESC LIMIT ?, ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Board.class), beginPoint, FinalPoint);
    }

    @Override
    public int getTotalBoardsCount() {
        String sql = "SELECT count(*) FROM board";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}