package techit.boardmvc.repository;

import techit.boardmvc.domain.Board;

import java.util.List;

public interface PaginationAndSortingRepository {
    List<Board> getSortedBoardPage(int page, int size);
    int getTotalBoardsCount();
}