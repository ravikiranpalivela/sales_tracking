package com.tekskills.st_tekskills.data.remote

import okhttp3.RequestBody
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
import com.tekskills.st_tekskills.data.model.ManagementResponse
import com.tekskills.st_tekskills.data.model.MeetingPurposeResponse
import com.tekskills.st_tekskills.data.model.MeetingPurposeResponseData
import com.tekskills.st_tekskills.data.model.MeetingStatusRequest
import com.tekskills.st_tekskills.data.model.MomProjectResponse
import com.tekskills.st_tekskills.data.model.MomProjectResponseItem
import com.tekskills.st_tekskills.data.model.NewClientResponse
import com.tekskills.st_tekskills.data.model.OpportunityByProjectIDResponse
import com.tekskills.st_tekskills.data.model.PendingActionItemGraphByIDResponse
import com.tekskills.st_tekskills.data.model.PendingEscalationGraphResponse
import com.tekskills.st_tekskills.data.model.PracticeHeadResponse
import com.tekskills.st_tekskills.data.model.ProjectListResponse
import com.tekskills.st_tekskills.data.model.ProjectManagerResponse
import com.tekskills.st_tekskills.data.model.ProjectOpportunityResponse
import com.tekskills.st_tekskills.data.model.UserAllowanceResponse
import com.tekskills.st_tekskills.data.model.UserMeResponse
import com.tekskills.st_tekskills.utils.recording.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
    private val apiService: ApiService
) : ApiHelper {


    override suspend fun userLogin(user: Map<String, String>): Response<LoginResponse> =
        apiService.userLogin(user)

    override suspend fun userChangePassword(
        authorization: String, password: String) = apiService.userChangePassword(authorization, password)

    override suspend fun userForgotPassword(userName: String,
    ) = apiService.userForgotPassword(userName)

    override suspend fun userForgotUsername(email: String,
    ) = apiService.userForgotUsername(email)

    override suspend fun getEmployeeMe(authorization: String): Response<UserMeResponse> =
        apiService.getEmployeeMe(authorization)


    override suspend fun getEmployeeAllowences(authorization: String, itemID:String)
    : Response<UserAllowanceResponse> = apiService.getEmployeeAllowences(authorization, itemID)


    override suspend fun getMeetingPurpose(authorization: String): Response<MeetingPurposeResponse> =
        apiService.getMeetingPurpose(authorization)

    override suspend fun getMeetingPurposeByID(authorization: String, itemID:String): Response<MeetingPurposeResponseData>
    = apiService.getMeetingPurposeByID(authorization, itemID)

    override suspend fun getMeetingPurposeByStatus(authorization: String,itemID:String): Response<MeetingPurposeResponse>
    = apiService.getMeetingPurposeByStatus(authorization, itemID)

    override suspend fun getMeetingPurposeStatus(authorization: String,): Response<MeetingStatusRequest>
    = apiService.getMeetingPurposeStatus(authorization)


    override suspend fun addTravelExpense(
       authorization: String, purposeID: RequestBody, amount: Long,
       file: MutableList<MultipartBody.Part>?, user: Map<String, RequestBody>
    ): Response<AddTravelExpenceResponse>
           = apiService.addTravelExpense(authorization, purposeID,amount, file, user)

    override suspend fun addHotelExpense(authorization: String, purposeID: RequestBody,
                                          file: MultipartBody.Part, user: Map<String, RequestBody>
    ): Response<AddTravelExpenceResponse>
            = apiService.addHotelExpense(authorization, purposeID, file, user)

    override suspend fun addFoodExpense(authorization: String, purposeID: RequestBody,
                                         user: Map<String, RequestBody>
    ): Response<AddTravelExpenceResponse>
            = apiService.addFoodExpense(authorization, purposeID, user)


    override suspend fun addMOMToMeeting(authorization: String,
                                purposeID: RequestBody, file: MutableList<MultipartBody.Part>?,
                                user: Map<String, RequestBody>
    ): Response<AddMOMResponse> = apiService.addMOMToMeeting(authorization, purposeID, file, user)

    override suspend fun addMeetingPurpose(
        authorization: String, user: AddMeetingRequest
    ): Response<AddPurposeMeetingResponse> = apiService.addMeetingPurpose(authorization, user)

    override suspend fun addUserCoordinates(
       authorization: String,
       user: AddLocationCoordinates
    ): Response<LocationResponse> = apiService.addUserCoordinates(authorization, user)

    /**
     * Dashboard Graph items
     */
    override suspend fun getPendingActionGraph(authorization: String): Response<PendingActionGraphResponse> =
        apiService.getPendingActionGraph(authorization)

    override suspend fun getEscalationGraph(authorization: String): Response<PendingEscalationGraphResponse> =
        apiService.getEscalationGraph(authorization)

    override suspend fun getPendingActionGraphByID(authorization: String, itemID:String): Response<PendingActionItemGraphByIDResponse> =
         apiService.getPendingActionGraphByID(authorization, itemID)

    override suspend fun getEscalationGraphByID(authorization: String, itemID:String): Response<ClientEscalationGraphByIDResponse>

    = apiService.getEscalationGraphByID(authorization,itemID)

    /**
     * ADD Opportunity
     */
    override suspend fun addOpportunity(
        authorization: String,
        requestBody: AddOpportunityRequest
    ): Response<NewClientResponse> =
        apiService.addOpportunity(authorization, requestBody)



    override suspend fun addClient(authorization: String, user: Map<String, String>)
            : Response<NewClientResponse> = apiService.addClient(authorization, user)

    override suspend fun getClients(authorization: String): Response<ClientNamesResponse> =
        apiService.getClients(authorization)

    override suspend fun getLeads(authorization: String, itemID:String): Response<LeadNamesResponse> =
        apiService.getLeads(authorization,itemID)

    override suspend fun putUserMeetingCheckIN(authorization: String, itemID:String,
                                               requestBody: MutableMap<String, RequestBody>,
                                               listMultipartImage: MultipartBody.Part): Response<LocationResponse> =
        apiService.putUserMeetingCheckIN(authorization, itemID,requestBody,listMultipartImage)

    override suspend fun putUserMeetingCheckOUT(authorization: String, itemID:String,requestBody: MutableMap<String, RequestBody>): Response<LocationResponse> =
        apiService.putUserMeetingCheckOUT(authorization, itemID,requestBody)

    override suspend fun getProjects(authorization: String): Response<ProjectListResponse> =
        apiService.getProjects(authorization)

    override suspend fun getAssignProjects(authorization: String): Response<AssignProjectListResponse> =
        apiService.getAssignProjects(authorization)



    override suspend fun getManagementList(authorization: String): Response<ManagementResponse> =
        apiService.getManagementList(authorization)

    override suspend fun getAccountManagerList(authorization: String): Response<AccountHeadResponse> =
        apiService.getAccountManagerList(authorization)

    override suspend fun getProjectManagerList(authorization: String): Response<ProjectManagerResponse> =
        apiService.getProjectManagerList(authorization)

    override suspend fun getPracticeHeadList(authorization: String): Response<PracticeHeadResponse> =
        apiService.getPracticeHeadList(authorization)

    override suspend fun getProjectOpportunity(authorization: String): Response<ProjectOpportunityResponse> =
        apiService.getProjectOpportunity(authorization)

    override suspend fun getMOMProjects(
        authorization: String,
        itemID: String
    ): Response<MomProjectResponse> =
        apiService.getMOMProjects(authorization, itemID)

    override suspend fun getMOMActionItemDetailsBYId(authorization: String,itemID:String): Response<MomProjectResponseItem> =
        apiService.getMOMActionItemDetailsBYId(authorization, itemID)

    override suspend fun getClientExist(authorization: String): Response<NewClientResponse> =
        apiService.getClientExist(authorization)

    override suspend fun getActionItemProjects(authorization: String): Response<NewClientResponse> =
        apiService.getActionItemProjects(authorization)

    override suspend fun getActionItemProjectID(
        authorization: String,
        itemID: String
    ): Response<ActionItemProjectIDResponse> =
        apiService.getActionItemProjectID(authorization, itemID)

    override suspend fun getActionItemBYID(authorization: String, itemID:String): Response<ActionItemProjectIDResponseItem> =
        apiService.getActionItemBYID(authorization, itemID)

    override suspend fun getProjectID(authorization: String): Response<ProjectListResponse> =
        apiService.getProjectID(authorization)

    override suspend fun getOpportunityByProductID(
        authorization: String,
        itemID: String
    ): Response<OpportunityByProjectIDResponse> =
        apiService.getOpportunityByProductID(authorization, itemID)


    override suspend fun getEscalationItemProjectID(
        authorization: String,
        itemID: String
    ): Response<ClientsEscalationResponse> =
        apiService.getEscalationItemProjectID(authorization, itemID)

    override suspend fun getCommentProjectID(
        authorization: String,
        itemID: String
    ): Response<CommentsListResponse> =
        apiService.getCommentProjectID(authorization, itemID)


    override suspend fun addProject(authorization: String, user: Map<String, String>)
            : Response<NewClientResponse> =
        apiService.addProject(authorization, user)

    override suspend fun addEmployee(authorization: String, user: Map<String, String>)
            : Response<NewClientResponse> =
        apiService.addEmployee(authorization, user)

    override suspend fun assignProject(authorization: String, user: Map<String, String>)
            : Response<NewClientResponse> = apiService.assignProject(authorization, user)

    override suspend fun addClientEscalation(authorization: String, user: AddEscalationRequest)
            : Response<NewClientResponse> = apiService.addClientEscalation(authorization, user)

    override suspend fun addMOMActionItem(
        authorization: String, user: AddMOMOpportunityRequest
    ): Response<NewClientResponse> =
        apiService.addMOMActionItem(authorization, user)

    override suspend fun addActionItemOpportunity(
        authorization: String,
        user: AddActionItemOpportunityRequest
    ): Response<NewClientResponse> = apiService.addActionItemOpportunity(authorization, user)

    override suspend fun addCommentOpportunity(
        authorization: String,
        user: AddCommentOpportunity
    ): Response<NewClientResponse> = apiService.addCommentOpportunity(authorization, user)

    override suspend fun uploadAudioFile(
        authorization: String,
        purposeId: RequestBody,
        callForm: RequestBody,
        callTo: RequestBody,
        callType: RequestBody,
        startAt: RequestBody,
        file: MultipartBody.Part
    ): Response<UploadResponse> = apiService.uploadAudioFile(authorization, purposeId, callForm, callTo, callType, startAt, file)

}