package community.dto;

import lombok.Data;

import java.util.List;

@Data
public class NotificationPageDTO {
    private Integer pages;
    private Integer pageNum;
    private Integer navigateFirstPage;
    private Integer navigateLastPage;
    private Boolean hasPreviousPage;
    private Boolean hasNextPage;
    private int[] navigatepageNums;

    private List<NotificationDTO> list;
}
