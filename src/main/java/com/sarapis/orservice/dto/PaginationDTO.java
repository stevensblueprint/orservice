package com.sarapis.orservice.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginationDTO<T> {
    private int totalItems;
    private int totalPages;
    private int pageNumber;
    private int size;
    private boolean firstPage;
    private boolean lastPage;
    private boolean empty;
    private List<T> contents;

    public static <T> PaginationDTO<T> of(int totalItems,
                                          int totalPages,
                                          int pageNumber,
                                          int size,
                                          boolean firstPage,
                                          boolean lastPage,
                                          boolean empty,
                                          List<T> contents) {
        PaginationDTO<T> paginationDTO = new PaginationDTO<>();
        paginationDTO.setTotalItems(totalItems);
        paginationDTO.setTotalPages(totalPages);
        paginationDTO.setPageNumber(pageNumber);
        paginationDTO.setSize(size);
        paginationDTO.setFirstPage(firstPage);
        paginationDTO.setLastPage(lastPage);
        paginationDTO.setEmpty(empty);
        paginationDTO.setContents(contents);
        return paginationDTO;
    }

    public static <T> PaginationDTO<T> of(List<T> contents, int pageNumber, int perPage) {
        int totalItems = contents.size();
        int totalPages = (int) Math.ceil((double) totalItems / perPage);

        // Get only the content that will be shown
        List<T> contentSubset = new ArrayList<>();
        for(int i = (pageNumber - 1) * perPage; i < Math.min(pageNumber * perPage, totalItems); i++) {
            contentSubset.add(contents.get(i));
        }

        int pageSize = contentSubset.size();
        boolean isFirstPage = pageNumber == 1;
        boolean isLastPage = pageNumber == totalPages;
        boolean isEmpty = pageSize == 0;

        return PaginationDTO.of(
                totalItems,         // total_items (Query Result Size)
                totalPages,         // total_pages
                pageNumber,         // page_number [User-provided]
                pageSize,           // size: (Query Result to be shown on this page_number)
                isFirstPage,        // first_page
                isLastPage,         // last_page
                isEmpty,            // empty
                contentSubset       // content (for this page_number)
        );
    }
}
