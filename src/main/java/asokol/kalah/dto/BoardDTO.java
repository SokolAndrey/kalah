package asokol.kalah.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

/**
 * DTO for board state.
 */
@Data
@Builder
public class BoardDTO {
    int firstHouse;
    int secondHouse;
    @Singular
    List<Integer> firstPits;
    @Singular
    List<Integer> secondPits;
}
