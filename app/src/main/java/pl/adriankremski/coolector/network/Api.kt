package pl.adriankremski.coolector.network

import io.reactivex.Observable
import pl.adriankremski.coolector.model.*
import retrofit2.Response
import retrofit2.http.*

interface Api {

    // GET
    @Headers("Accept: application/json", "Content-type: application/json")
    @GET("api/remarks/categories")
    fun remarkCategories(): Observable<List<RemarkCategory>>

    @Headers("Accept: application/json", "Content-type: application/json")
    @GET("api/remarks")
    fun remarks(@Query("latest") latest: Boolean, @Query("orderBy") orderBy: String, @Query("sortorder") sortorder: String, @Query("results") results: Int): Observable<List<Remark>>

    @Headers("Accept: application/json", "Content-type: application/json")
    @GET("api/remarks/tags")
    fun remarkTags(): Observable<List<RemarkTag>>

    @Headers("Accept: application/json", "Content-type: application/json")
    @GET("api/{path}")
    fun createdRemark(@Path(value = "path", encoded = true) resourcePath: String): Observable<RemarkNotFromList>

    @Headers("Accept: application/json", "Content-type: application/json")
    @GET("api/{path}")
    fun operation(@Path(value = "path", encoded = true) operationPath: String): Observable<Operation>

    // POST
    @Headers("Accept: application/json", "Content-type: application/json")
    @POST("api/sign-in")
    fun login(@Body authRequest: AuthRequest): Observable<AuthResponse>

    @Headers("Accept: application/json", "Content-type: application/json")
    @POST("api/sign-up")
    fun signUp(@Body authRequest: SignUpRequest): Observable<Response<Void>>

    @Headers("Accept: application/json", "Content-type: application/json")
    @POST("api/reset-password")
    fun resetPassword(@Body body: ResetPasswordRequest): Observable<Response<Void>>

    @Headers("Accept: application/json", "Content-type: application/json")
    @POST("api/remarks")
    fun saveRemark(@Body remark: NewRemark): Observable<Response<Void>>
}
