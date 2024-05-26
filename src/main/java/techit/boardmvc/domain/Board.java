package techit.boardmvc.domain;

import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("board")
@Data
public class Board {
    @Id // Primary key
    private Long id;

    /**
     *  이름, 제목, 암호, 본문, 등록일, 수정일
     */
    private String name;
    private String title;
    private String password;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}