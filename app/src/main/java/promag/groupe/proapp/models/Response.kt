package promag.groupe.proapp.models

class PageResponse<T>(
    var count: Int,
    var next: String,
    var previous: String,
    var results: List<T>
)