package org.hlopes.dtos.response;

import java.util.List;

public record PagedResult<T>(List<T> list, long totalCount) {
}
