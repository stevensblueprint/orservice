package com.sarapis.orservice.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginationDTO<T> {
  private int totalItems;
  private int totalPages;
  private int pageNumber;
  private int size;
  private boolean firstPage;
  private boolean lastPage;
  private boolean empty;
  private List<T> contents;
}
