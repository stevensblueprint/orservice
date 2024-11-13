package com.sarapis.orservice.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
      int totalPages, int pageNumber, int size,
      boolean firstPage, boolean lastPage, boolean empty, List<T> contents) {
    PaginationDTO<T> paginationDTO = new PaginationDTO<>();
    paginationDTO.setTotalItems(totalItems);
    paginationDTO.setTotalItems(totalPages);
    paginationDTO.setPageNumber(pageNumber);
    paginationDTO.setSize(size);
    paginationDTO.setFirstPage(firstPage);
    paginationDTO.setLastPage(lastPage);
    paginationDTO.setEmpty(empty);
    paginationDTO.setContents(contents);
    return paginationDTO;
  }
}
