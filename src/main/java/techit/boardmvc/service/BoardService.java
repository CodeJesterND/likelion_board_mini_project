package techit.boardmvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.boardmvc.domain.Board;
import techit.boardmvc.repository.BoardRepository;
import static techit.boardmvc.util.BCryptPasswordSecurity.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public List<Board> getBoards(int page, int size) {
        return boardRepository.getSortedBoardPage(page, size);
    }

    @Transactional
    public Board updateByBoard(Board board) {
        return boardRepository.save(board);
    }

    @Transactional
    public void deleteByBoard(Long id) {
        boardRepository.deleteById(id);
    }

    @Transactional
    public Board saveByBoard(Board board) {
        return boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public int getTotalBoardCount() {
        return boardRepository.getTotalBoardsCount();
    }

    @Transactional(readOnly = true)
    public Optional<Board> findBoardByID(Long id) {
        return boardRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public boolean validatePassword(Long id, String password) {
        Optional<Board> optionalBoard = findBoardByID(id);
        return optionalBoard.map(board -> board.getPassword().equals(password)).orElse(false);
    }

    public String hashedBoardByPassword(String password) {
        String hashedPassword =  hashingPassword(password);
        return hashedPassword;
    }

    public boolean validatePassword(String notHashPassword, String hashedPassword) {
        return verifyPassword(notHashPassword, hashedPassword);
    }
}