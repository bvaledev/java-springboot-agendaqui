package br.dev.brendo.agendaqui.module.pagination;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PaginatedOutputDTO<T> {
    @Schema(example = "[]", requiredMode = Schema.RequiredMode.REQUIRED)
    public List<T> data;
    @Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    public int currentPage;
    @Schema(example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    public long totalItems;
    @Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    public int totalPages;

    public PaginatedOutputDTO(List<T> data, Pageable page) {
        this.data = data;
        this.currentPage = page.getPageNumber();
        this.totalItems = page.getPageSize();
        this.totalPages = page.getPageSize();
    }
}
