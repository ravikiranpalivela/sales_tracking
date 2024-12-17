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
import com.tekskills.st_tekskills.data.model.ManagementResponse
import com.tekskills.st_tekskills.data.model.MeetingPurposeResponse
import com.tekskills.st_tekskills.data.model.MeetingPurposeResponseData
import com.tekskills.st_tekskills.data.model.MeetingStatusRequest
import com.tekskills.st_tekskills.data.model.MessageResponse
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
import okhttp3.RequestBody
import retrofit2.Response

interface ApiHelper {

    suspend fun userLogin(user: Map<String, String>): Response<LoginResponse>

    suspend fun userChangePassword(
        authorization: String, password: String): Response<MessageResponse>

    suspend fun userForgotPassword(
        userName: String,
    ): Response<MessageResponse>


    suspend fun userForgotUsername(
        email: String,
    ): Response<MessageResponse>

    suspend fun getEmployeeMe(authorization: String): Response<UserMeResponse>

    suspend fun getEmployeeAllowences(
        authorization: String,
        itemID: String
    ): Response<UserAllowanceResponse>

    suspend fun addMeetingPurpose(
        authorization: String, user: AddMeetingRequest
    ): Response<AddPurposeMeetingResponse>


    suspend fun addUserCoordinates(
        authorization: String,
        user: AddLocationCoordinates
    ): Response<LocationResponse>

    suspend fun getMeetingPurpose(authorization: String): Response<MeetingPurposeResponse>

    suspend fun getMeetingPurposeByID(
        authorization: String,
        itemID: String
    ): Response<MeetingPurposeResponseData>

    suspend fun getMeetingPurposeByStatus(authorization: String,itemID:String): Response<MeetingPurposeResponse>

    suspend fun getMeetingPurposeStatus(authorization: String,): Response<MeetingStatusRequest>


    suspend fun addTravelExpense(
        authorization: String, purposeID: RequestBody, amount: Long,
        file: MutableList<MultipartBody.Part>?, user: Map<String, RequestBody>
    ): Response<AddTravelExpenceResponse>

    suspend fun addHotelExpense(
        authorization: String, purposeID: RequestBody,
        file: MultipartBody.Part, user: Map<String, RequestBody>
    ): Response<AddTravelExpenceResponse>


    suspend fun addFoodExpense(
        authorization: String, purposeID: RequestBody,
        user: Map<String, RequestBody>
    ): Response<AddTravelExpenceResponse>


    suspend fun addMOMToMeeting(
        authorization: String,
        purposeID: RequestBody, file: MutableList<MultipartBody.Part>?,
        user: Map<String, RequestBody>
    ): Response<AddMOMResponse>

    /**
     * Dashboard Graph items
     */
    suspend fun getPendingActionGraph(authorization: String): Response<PendingActionGraphResponse>

    suspend fun getEscalationGraph(authorization: String): Response<PendingEscalationGraphResponse>

    suspend fun getPendingActionGraphByID(
        authorization: String,
        itemID: String
    ): Response<PendingActionItemGraphByIDResponse>

    suspend fun getEscalationGraphByID(
        authorization: String,
        itemID: String
    ): Response<ClientEscalationGraphByIDResponse>


    /**
     * ADD Opportunity
     */
    suspend fun addOpportunity(
        authorization: String,
        requestBody: AddOpportunityRequest
    ): Response<NewClientResponse>


    suspend fun addClient(authorization: String, user: Map<String, String>)
            : Response<NewClientResponse>

    suspend fun getClients(authorization: String): Response<ClientNamesResponse>

    suspend fun getLeads(authorization: String, itemID: String): Response<LeadNamesResponse>

    suspend fun putUserMeetingCheckIN(
        authorization: String, itemID: String,
        requestBody: MutableMap<String, RequestBody>,
        listMultipartImage: MultipartBody.Part
    ): Response<LocationResponse>

    suspend fun putUserMeetingCheckOUT(
        authorization: String,
        itemID: String,
        requestBody: MutableMap<String, RequestBody>
    ): Response<LocationResponse>

    suspend fun getProjects(authorization: String): Response<ProjectListResponse>

    suspend fun getAssignProjects(authorization: String): Response<AssignProjectListResponse>


    suspend fun getManagementList(authorization: String): Response<ManagementResponse>
    suspend fun getAccountManagerList(authorization: String): Response<AccountHeadResponse>
    suspend fun getProjectManagerList(authorization: String): Response<ProjectManagerResponse>
    suspend fun getPracticeHeadList(authorization: String): Response<PracticeHeadResponse>

    suspend fun getProjectOpportunity(authorization: String): Response<ProjectOpportunityResponse>

    suspend fun getMOMProjects(authorization: String, itemID: String): Response<MomProjectResponse>

    suspend fun getMOMActionItemDetailsBYId(
        authorization: String,
        itemID: String
    ): Response<MomProjectResponseItem>

    suspend fun getOpportunityByProductID(
        authorization: String,
        itemID: String
    ): Response<OpportunityByProjectIDResponse>

    suspend fun getClientExist(authorization: String): Response<NewClientResponse>

    suspend fun getActionItemProjects(authorization: String): Response<NewClientResponse>

    suspend fun getActionItemProjectID(
        authorization: String,
        itemID: String
    ): Response<ActionItemProjectIDResponse>

    suspend fun getActionItemBYID(
        authorization: String,
        itemID: String
    ): Response<ActionItemProjectIDResponseItem>

    suspend fun getProjectID(authorization: String): Response<ProjectListResponse>

    suspend fun getEscalationItemProjectID(
        authorization: String,
        itemID: String
    ): Response<ClientsEscalationResponse>

    suspend fun getCommentProjectID(
        authorization: String,
        itemID: String
    ): Response<CommentsListResponse>

    suspend fun addProject(authorization: String, user: Map<String, String>)
            : Response<NewClientResponse>

    suspend fun addEmployee(authorization: String, user: Map<String, String>)
            : Response<NewClientResponse>

    suspend fun assignProject(authorization: String, user: Map<String, String>)
            : Response<NewClientResponse>

    suspend fun addClientEscalation(authorization: String, user: AddEscalationRequest)
            : Response<NewClientResponse>

    suspend fun addMOMActionItem(
        authorization: String,
        user: AddMOMOpportunityRequest
    ): Response<NewClientResponse>

    suspend fun addActionItemOpportunity(
        authorization: String,
        user: AddActionItemOpportunityRequest
    ): Response<NewClientResponse>

    suspend fun addCommentOpportunity(
        authorization: String,
        user: AddCommentOpportunity
    ): Response<NewClientResponse>

    suspend fun uploadAudioFile(
        authorization: String,
        purposeId: RequestBody,
        callForm: RequestBody,
        callTo: RequestBody,
        callType: RequestBody,
        startAt: RequestBody,
        file: MultipartBody.Part
    ): Response<UploadResponse>
}