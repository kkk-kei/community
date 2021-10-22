package community.dto;

import community.model.Question;
import lombok.Data;

import java.util.List;

@Data
public class QuestionPageDTO {
    private Integer pages;
    private Integer pageNum;
    private Integer navigateFirstPage;
    private Integer navigateLastPage;
    private Boolean hasPreviousPage;
    private Boolean hasNextPage;
    private int[] navigatepageNums;

    private List<QuestionDTO> list;
}
