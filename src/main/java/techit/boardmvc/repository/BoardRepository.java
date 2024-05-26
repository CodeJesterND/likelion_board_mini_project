package techit.boardmvc.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import techit.boardmvc.domain.Board;

@Repository
public interface BoardRepository extends CrudRepository<Board, Long>, PaginationAndSortingRepository {
}