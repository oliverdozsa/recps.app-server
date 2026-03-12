package app.recps.rest.responses;

import java.util.List;

public record PageResponse<T>(List<T> items) {
}
