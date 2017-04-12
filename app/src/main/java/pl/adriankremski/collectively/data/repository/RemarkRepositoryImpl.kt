package pl.adriankremski.collectively.data.repository

import io.reactivex.Observable
import pl.adriankremski.collectively.data.cache.RemarkCategoriesCache
import pl.adriankremski.collectively.data.datasource.RemarksDataSource
import pl.adriankremski.collectively.data.model.*
import pl.adriankremski.collectively.data.repository.util.OperationRepository

class RemarkRepositoryImpl(val remarkCategoriesCache: RemarkCategoriesCache,
                           val remarksDataSource: RemarksDataSource,
                           val operationRepository: OperationRepository) : RemarksRepository {

    override fun loadRemarkCategories(): Observable<List<RemarkCategory>> {
        if (remarkCategoriesCache.isExpired()) {
            return remarksDataSource.loadRemarkCategories().doOnNext { remarkCategoriesCache.putData(it) }
        } else {
            return remarkCategoriesCache.getData()
        }
    }

    override fun loadRemarks(): Observable<List<Remark>> = remarksDataSource.loadRemarks()

    override fun loadRemark(id: String): Observable<RemarkPreview> = remarksDataSource.loadRemarkPreview(id)

    override fun saveRemark(remark: NewRemark): Observable<RemarkNotFromList> =
            operationRepository.pollOperation(remarksDataSource.saveRemark(remark)).flatMap { remarksDataSource.loadSavedRemark(it.resource) }

    override fun loadRemarkTags(): Observable<List<RemarkTag>> = remarksDataSource.loadRemarkTags()
}

