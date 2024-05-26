package techit.boardmvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

import techit.boardmvc.domain.Board;
import techit.boardmvc.exception.BoardNotFoundException;
import techit.boardmvc.service.BoardService;

import static techit.boardmvc.config.AppConstants.*;
import static techit.boardmvc.util.BCryptPasswordSecurity.*;

@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;

    @GetMapping("/list")
    public String listBoard(Model model,
                            @RequestParam(defaultValue = "" + DEFAULT_PAGE) int page,
                            @RequestParam(defaultValue = "" + DEFAULT_SIZE) int size) {

        List<Board> boards = boardService.getBoards(page, size);
        int totalBoards = boardService.getTotalBoardCount();
        int totalPages = (int) Math.ceil((double) totalBoards / size);

        model.addAttribute("boards", boards);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);

        return "board/list";
    }

    @GetMapping("/view")
    public String viewBoard(@RequestParam("id") Long id,
                            @RequestParam(value = "page", defaultValue = "" + DEFAULT_PAGE) int page,
                            Model model) {

        Board board = boardService.findBoardByID(id)
                .orElseThrow(() -> new BoardNotFoundException("ID가 " + id + "인 게시판을 찾을 수 없습니다."));

        model.addAttribute("board", board);
        model.addAttribute("currentPage", page);

        return "board/view";
    }

    @GetMapping("/writeform")
    public String writeForm(Model model) {
        model.addAttribute("board", new Board());
        return "board/writeform";
    }

    @PostMapping("/write")
    public String write(Board board) {
        board.setCreatedAt(LocalDateTime.now(DEFAULT_ZONE));
        String hashedPassword = hashingPassword(board.getPassword());
        board.setPassword(hashedPassword);
        boardService.saveByBoard(board);
        return "redirect:/list";
    }

    @GetMapping("/updateform")
    public String updateForm(@RequestParam("id") Long id, @RequestParam("page") int page, Model model) {
        Board board = boardService.findBoardByID(id).orElse(null);
        model.addAttribute("board", board);
        model.addAttribute("page", page);
        return "board/updateform";
    }

    @PostMapping("/update")
    public String update(Board board,
                         @RequestParam("id") Long id,
                         @RequestParam("page") int page,
                         Model model) {
        Board originalBoard = boardService.findBoardByID(id)
                .orElseThrow(() -> new BoardNotFoundException("ID가 " + id + "인 게시판을 찾을 수 없습니다."));

        if (!verifyPassword(board.getPassword(), originalBoard.getPassword())) {
            model.addAttribute("errorMessage", true);
            model.addAttribute("page", page);
            model.addAttribute("board", board);
            return "board/updateform";
        }

        String hashedPassword = hashingPassword(board.getPassword());
        board.setPassword(hashedPassword);

        board.setUpdatedAt(LocalDateTime.now(DEFAULT_ZONE));
        boardService.updateByBoard(board);
        return "redirect:/view?id=" + board.getId() + "&page=" + page;
    }

    @GetMapping("/deleteform")
    public String deleteForm(@RequestParam("id") Long id,
                             @RequestParam("page") int page,
                             Model model) {
        model.addAttribute("id", id);
        model.addAttribute("page", page);
        return "board/deleteform";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") Long id,
                         @RequestParam("password") String password,
                         @RequestParam("page") int page) {
        Board board = boardService.findBoardByID(id)
                .orElseThrow(() -> new BoardNotFoundException("ID가 " + id + "인 게시판을 찾을 수 없습니다."));

        if (verifyPassword(password, board.getPassword())) {
            boardService.deleteByBoard(id);
            return "redirect:/list";
        }
        return "redirect:/deleteform?id=" + id + "&page=" + page + "&error=true";
    }
}