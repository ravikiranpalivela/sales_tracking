package com.tekskills.st_tekskills.data.remote

import com.tekskills.st_tekskills.data.model.AccountHeadResponse
import com.tekskills.st_tekskills.data.model.ActionItemProjectIDResponse
import com.tekskills.st_tekskills.data.model.ActionItemProjectIDResponseItem
import com.tekskills.st_tekskills.data.model.AddActionItemOpportunityRequest
import com.tekskills.st_tekskills.data.model.AddCommentOpportunity
import com.tekskills.st_tekskills.data.model.AddEscalationRequest
import com.tekskills.st_tekskills.data.model.AddLocationCoordinates
import com.tekskills.st_tekskills.data.model.AddMOMOpportunityRequest
import com.tekskills.st_tekskills.data.model.AddMOMResponse
import com.tekskills.st_tekskills.data.model.AddOpportunityRequest
import com.tekskills.st_tekskills.data.model.AddMeetingRequest
import com.tekskills.st_tekskills.data.model.AddPurposeMeetingResponse
import com.tekskills.st_tekskills.data.model.AddTravelExpenceResponse
import com.tekskills.st_tekskills.data.model.AssignProjectListResponse
import com.tekskills.st_tekskills.data.model.ClientEscalationGraphByIDResponse
import com.tekskills.st_tekskills.data.model.PendingActionGraphResponse
import com.tekskills.st_tekskills.data.model.ClientNamesResponse
import com.tekskills.st_tekskills.data.model.ClientsEscalationResponse
import com.tekskills.st_tekskills.data.model.CommentsListResponse
import com.tekskills.st_tekskills.data.model.LeadNamesResponse
import com.tekskills.st_tekskills.data.model.LocationResponse
import com.tekskills.st_tekskills.data.model.LoginResponse
import com.tekskills.st_tekskills.data.model.MomProjectResponse
import com.tekskills.st_tekskills.data.model.NewClientResponse
import com.tekskills.st_tekskills.data.model.ProjectListResponse
import com.tekskills.st_tekskills.data.model.ManagementResponse
import com.tekskills.st_tekskills.data.model.MeetingPurposeResponse
import com.tekskills.st_tekskills.data.model.MeetingPurposeResponseData
import com.tekskills.st_tekskills.data.model.MeetingStatusRequest
import com.tekskills.st_tekskills.data.model.MessageResponse
import com.tekskills.st_tekskills.data.model.MomProjectResponseItem
import com.tekskills.st_tekskills.data.model.OpportunityByProjectIDResponse
import com.tekskills.st_tekskills.data.model.PendingActionItemGraphByIDResponse
import com.tekskills.st_tekskills.data.model.PendingEscalationGraphResponse
import com.tekskills.st_tekskills.data.model.PracticeHeadResponse
import com.tekskills.st_tekskills.data.model.ProjectManagerResponse
import com.tekskills.st_tekskills.data.model.ProjectOpportunityResponse
import com.tekskills.st_tekskills.data.model.UserAllowanceResponse
import com.tekskills.st_tekskills.data.model.UserMeResponse
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.GET_ACCOUNT_MANAGER
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.GET_ACTION_ITEM_BY_ID
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.GET_ACTION_ITEM_PROJECT
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.GET_ACTION_ITEM_PROJECT_ID
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.GET_ASSIGN_PROJECTS
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.GET_CLIENTS
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.GET_CLIENT_ESCALATION_BY_ID
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.GET_CLIENT_ESCALATION_GRAPH_BY_ID
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.GET_CLIENT_EXIST
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.GET_COMMENT_BY_ID
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.GET_LEADS
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.GET_ME
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.GET_MEETING_PURPOSE_BY_ID
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.GET_MEETING_PURPOSE_BY_STATUS
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.GET_MEETING_PURPOSE_STATUS
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.GET_MEETING_PURPOSE_VISIT_DETAILS
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.GET_MOM_ACTION_ITEM_BY_ID
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.GET_PENDING_ACTION_GRAPH
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.GET_MOM_PROJECT
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.GET_OPPORTUNITY_BY_PROJECT_ID
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.GET_PENDING_ACTION_GRAPH_BY_ID
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.GET_PENDING_ESCALATION_GRAPH
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.GET_PRACTICE_MANAGER
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.GET_PROJECTS
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.GET_PROJECT_ID
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.GET_PROJECT_MANAGER
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.GET_SRMANAGEMENT
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.GET_USER_ALLOWENCE
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.POST_ADD_MOM_MEETING
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.POST_ADD_OPPORTUNITY
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.POST_SAVE_CALL
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.POST_TRAVEL_EXPENSES
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.PUT_CHANGE_PASSWORD
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.PUT_CHECK_IN
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.PUT_CHECK_OUT
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.PUT_FORGOT_PASSWORD
import com.tekskills.st_tekskills.data.remote.APIEndPoint.Companion.PUT_FORGOT_USERNAME
import com.tekskills.st_tekskills.utils.Common.Companion.APP_JSON
import com.tekskills.st_tekskills.utils.Common.Companion.AUTHORIZATION
import com.tekskills.st_tekskills.utils.recording.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @Headers(APP_JSON)
    @POST(APIEndPoint.POST_USER_LOGIN)
    suspend fun userLogin(
        @Body user: Map<String, String>
    ): Response<LoginResponse>

    @PUT(PUT_CHANGE_PASSWORD)
    suspend fun userChangePassword(
        @Header(AUTHORIZATION) authorization: String,
        @Query("pwd") password: String
    ): Response<MessageResponse>

    @PUT(PUT_FORGOT_PASSWORD)
    suspend fun userForgotPassword(
        @Query("userName") userName: String,
    ): Response<MessageResponse>

    @GET(PUT_FORGOT_USERNAME)
    suspend fun userForgotUsername(
        @Query("email") email: String,
    ): Response<MessageResponse>

    @GET(GET_ME)
    suspend fun getEmployeeMe(@Header(AUTHORIZATION) authorization: String): Response<UserMeResponse>

    @GET("${GET_USER_ALLOWENCE}/{id}")
    suspend fun getEmployeeAllowences(
        @Header(AUTHORIZATION) authorization: String,
        @Path("id") itemID: String
    ): Response<UserAllowanceResponse>

    @Headers(APP_JSON)
    @POST(APIEndPoint.POST_ADD_MEETING_PURPOSE)
    suspend fun addMeetingPurpose(
        @Header(AUTHORIZATION) authorization: String,
        @Body user: AddMeetingRequest
    ): Response<AddPurposeMeetingResponse>

    @Headers(APP_JSON)
    @POST(APIEndPoint.POST_ADD_LOCATION)
    suspend fun addUserCoordinates(
        @Header(AUTHORIZATION) authorization: String,
        @Body user: AddLocationCoordinates
    ): Response<LocationResponse>

    @GET(GET_MEETING_PURPOSE_VISIT_DETAILS)
    suspend fun getMeetingPurpose(@Header(AUTHORIZATION) authorization: String): Response<MeetingPurposeResponse>

    @GET("${GET_MEETING_PURPOSE_BY_STATUS}")
    suspend fun getMeetingPurposeByStatus(
        @Header(AUTHORIZATION) authorization: String,
        @Query("status") itemID: String
    ): Response<MeetingPurposeResponse>

    @GET("${GET_MEETING_PURPOSE_BY_ID}/{id}")
    suspend fun getMeetingPurposeByID(
        @Header(AUTHORIZATION) authorization: String,
        @Path("id") itemID: String
    ): Response<MeetingPurposeResponseData>

    @GET(GET_MEETING_PURPOSE_STATUS)
    suspend fun getMeetingPurposeStatus(@Header(AUTHORIZATION) authorization: String): Response<MeetingStatusRequest>


    //    @Headers(MULTI_PART)
    @Multipart
    @POST(POST_TRAVEL_EXPENSES)
    suspend fun addTravelExpense(
        @Header(AUTHORIZATION) authorization: String,
        @Part("purposeId") purposeID: RequestBody,
        @Part("amount") amount: Long,
        @Part file: MutableList<MultipartBody.Part>?,
        @PartMap user: Map<String, @JvmSuppressWildcards RequestBody>
    ): Response<AddTravelExpenceResponse>

    //    @Headers(MULTI_PART)
    @Multipart
    @POST(POST_TRAVEL_EXPENSES)
    suspend fun addHotelExpense(
        @Header(AUTHORIZATION) authorization: String,
        @Part("purposeId") purposeID: RequestBody,
        @Part file: MultipartBody.Part,
        @PartMap user: Map<String, @JvmSuppressWildcards RequestBody>
    ): Response<AddTravelExpenceResponse>

    //    @Headers(MULTI_PART)
    @Multipart
    @POST(POST_TRAVEL_EXPENSES)
    suspend fun addFoodExpense(
        @Header(AUTHORIZATION) authorization: String,
        @Part("purposeId") purposeID: RequestBody,
        @PartMap user: Map<String, @JvmSuppressWildcards RequestBody>
    ): Response<AddTravelExpenceResponse>


    @Multipart
    @POST(POST_ADD_MOM_MEETING)
    suspend fun addMOMToMeeting(
        @Header(AUTHORIZATION) authorization: String,
        @Part("purposeId") purposeID: RequestBody,
        @Part file: MutableList<MultipartBody.Part>?,
        @PartMap user: Map<String, @JvmSuppressWildcards RequestBody>
    ): Response<AddMOMResponse>

    @GET(GET_CLIENTS)
    suspend fun getClients(@Header(AUTHORIZATION) authorization: String): Response<ClientNamesResponse>

    @GET("${GET_LEADS}/{id}")
    suspend fun getLeads(
        @Header(AUTHORIZATION) authorization: String,
        @Path("id") itemID: String
    ): Response<LeadNamesResponse>

    @Multipart
    @PUT("${PUT_CHECK_IN}/{id}")
    suspend fun putUserMeetingCheckIN(
        @Header(AUTHORIZATION) authorization: String, @Path("id") itemID: String,
        @PartMap requestBody: MutableMap<String, RequestBody>,
        @Part listMultipartImage: MultipartBody.Part
    ): Response<LocationResponse>

    @Multipart
    @PUT("${PUT_CHECK_OUT}/{id}")
    suspend fun putUserMeetingCheckOUT(
        @Header(AUTHORIZATION) authorization: String,
        @Path("id") itemID: String,
        @PartMap requestBody: MutableMap<String, RequestBody>
    ): Response<LocationResponse>


    @Headers(APP_JSON)
    @POST(APIEndPoint.POST_ADD_COMMENT)
    suspend fun addCommentOpportunity(
        @Header(AUTHORIZATION) authorization: String,
        @Body user: AddCommentOpportunity
    ): Response<NewClientResponse>

    /**
     * Dashboard Graph items
     */
    @GET(GET_PENDING_ACTION_GRAPH)
    suspend fun getPendingActionGraph(@Header(AUTHORIZATION) authorization: String): Response<PendingActionGraphResponse>

    @GET(GET_PENDING_ESCALATION_GRAPH)
    suspend fun getEscalationGraph(@Header(AUTHORIZATION) authorization: String): Response<PendingEscalationGraphResponse>

    @GET("${GET_PENDING_ACTION_GRAPH_BY_ID}/{id}")
    suspend fun getPendingActionGraphByID(
        @Header(AUTHORIZATION) authorization: String,
        @Path("id") itemID: String
    ): Response<PendingActionItemGraphByIDResponse>

    @GET("${GET_CLIENT_ESCALATION_GRAPH_BY_ID}/{id}")
    suspend fun getEscalationGraphByID(
        @Header(AUTHORIZATION) authorization: String,
        @Path("id") itemID: String
    ): Response<ClientEscalationGraphByIDResponse>

    /**
     * ADD Opportunity
     */
    @POST(POST_ADD_OPPORTUNITY)
    suspend fun addOpportunity(
        @Header(AUTHORIZATION) authorization: String,
        @Body requestBody: AddOpportunityRequest
    ): Response<NewClientResponse>

    @GET(GET_PROJECTS)
    suspend fun getProjects(@Header(AUTHORIZATION) authorization: String): Response<ProjectListResponse>

    @GET(GET_ASSIGN_PROJECTS)
    suspend fun getAssignProjects(@Header(AUTHORIZATION) authorization: String): Response<AssignProjectListResponse>

    @GET(GET_SRMANAGEMENT)
    suspend fun getManagementList(@Header(AUTHORIZATION) authorization: String): Response<ManagementResponse>

    @GET(GET_ACCOUNT_MANAGER)
    suspend fun getAccountManagerList(@Header(AUTHORIZATION) authorization: String): Response<AccountHeadResponse>

    @GET(GET_PROJECT_MANAGER)
    suspend fun getProjectManagerList(@Header(AUTHORIZATION) authorization: String): Response<ProjectManagerResponse>

    @GET(GET_PRACTICE_MANAGER)
    suspend fun getPracticeHeadList(@Header(AUTHORIZATION) authorization: String): Response<PracticeHeadResponse>

    @GET("${GET_MOM_PROJECT}/{id}")
    suspend fun getMOMProjects(
        @Header(AUTHORIZATION) authorization: String,
        @Path("id") itemID: String
    ): Response<MomProjectResponse>

    @GET("${GET_MOM_ACTION_ITEM_BY_ID}/{id}")
    suspend fun getMOMActionItemDetailsBYId(
        @Header(AUTHORIZATION) authorization: String,
        @Path("id") itemID: String
    ): Response<MomProjectResponseItem>

    @GET(GET_CLIENT_EXIST)
    suspend fun getClientExist(@Header(AUTHORIZATION) authorization: String): Response<NewClientResponse>

    @GET(GET_ACTION_ITEM_PROJECT)
    suspend fun getActionItemProjects(@Header(AUTHORIZATION) authorization: String): Response<NewClientResponse>

    @GET("${GET_ACTION_ITEM_PROJECT_ID}/{id}")
    suspend fun getActionItemProjectID(
        @Header(AUTHORIZATION) authorization: String,
        @Path("id") itemID: String
    ): Response<ActionItemProjectIDResponse>

    @GET("${GET_ACTION_ITEM_BY_ID}/{id}")
    suspend fun getActionItemBYID(
        @Header(AUTHORIZATION) authorization: String,
        @Path("id") itemID: String
    ): Response<ActionItemProjectIDResponseItem>

    @GET("${GET_CLIENT_ESCALATION_BY_ID}/{id}")
    suspend fun getEscalationItemProjectID(
        @Header(AUTHORIZATION) authorization: String,
        @Path("id") itemID: String
    ): Response<ClientsEscalationResponse>

    @GET("${GET_COMMENT_BY_ID}/{id}")
    suspend fun getCommentProjectID(
        @Header(AUTHORIZATION) authorization: String,
        @Path("id") itemID: String
    ): Response<CommentsListResponse>

    @GET("${GET_OPPORTUNITY_BY_PROJECT_ID}/{id}")
    suspend fun getOpportunityByProductID(
        @Header(AUTHORIZATION) authorization: String,
        @Path("id") itemID: String
    ): Response<OpportunityByProjectIDResponse>

    @GET(GET_PROJECT_ID)
    suspend fun getProjectID(@Header(AUTHORIZATION) authorization: String): Response<ProjectListResponse>

    @GET(GET_PROJECT_ID)
    suspend fun getProjectOpportunity(@Header(AUTHORIZATION) authorization: String): Response<ProjectOpportunityResponse>

    @Headers(APP_JSON)
    @POST(APIEndPoint.POST_ADD_CLIENT)
    suspend fun addClient(
        @Header(AUTHORIZATION) authorization: String,
        @Body user: Map<String, String>
    ): Response<NewClientResponse>

    @Headers(APP_JSON)
    @POST(APIEndPoint.POST_ADD_PROJECT)
    suspend fun addProject(
        @Header(AUTHORIZATION) authorization: String,
        @Body user: Map<String, String>
    ): Response<NewClientResponse>

    @Headers(APP_JSON)
    @POST(APIEndPoint.POST_ADD_EMPLOYEE)
    suspend fun addEmployee(
        @Header(AUTHORIZATION) authorization: String,
        @Body user: Map<String, String>
    ): Response<NewClientResponse>

    @Headers(APP_JSON)
    @POST(APIEndPoint.POST_ASSIGN_PROJECT)
    suspend fun assignProject(
        @Header(AUTHORIZATION) authorization: String,
        @Body user: Map<String, String>
    ): Response<NewClientResponse>

    @Headers(APP_JSON)
    @POST(APIEndPoint.POST_CLIENT_ESCALATION)
    suspend fun addClientEscalation(
        @Header(AUTHORIZATION) authorization: String,
        @Body user: AddEscalationRequest
    ): Response<NewClientResponse>

    @Headers(APP_JSON)
    @POST(APIEndPoint.POST_ADD_MOM)
    suspend fun addMOMActionItem(
        @Header(AUTHORIZATION) authorization: String,
        @Body user: AddMOMOpportunityRequest
    ): Response<NewClientResponse>

    @Headers(APP_JSON)
    @POST(APIEndPoint.POST_ADD_Action_Item)
    suspend fun addActionItemOpportunity(
        @Header(AUTHORIZATION) authorization: String,
        @Body user: AddActionItemOpportunityRequest
    ): Response<NewClientResponse>


    @Multipart
    @POST(POST_SAVE_CALL)
    fun uploadAudioFile(
        @Header(AUTHORIZATION) authorization: String,
        @Part("purposeId") purposeId: RequestBody,
        @Part("callForm") callForm: RequestBody,
        @Part("callTo") callTo: RequestBody,
        @Part("callType") callType: RequestBody,
        @Part("startAt") startAt: RequestBody,
        @Part file: MultipartBody.Part
    ): Response<UploadResponse>

}