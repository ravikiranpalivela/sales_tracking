package com.tekskills.st_tekskills.data.repository

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
import com.tekskills.st_tekskills.data.model.AddTravelExpenceResponse
import com.tekskills.st_tekskills.data.model.ClientsEscalationResponse
import com.tekskills.st_tekskills.data.model.CommentsListResponse
import com.tekskills.st_tekskills.data.model.MeetingPurposeResponse
import com.tekskills.st_tekskills.data.model.MeetingPurposeResponseData
import com.tekskills.st_tekskills.data.model.MeetingStatusRequest
import com.tekskills.st_tekskills.data.model.MomProjectResponse
import com.tekskills.st_tekskills.data.model.MomProjectResponseItem
import com.tekskills.st_tekskills.data.model.NewClientResponse
import com.tekskills.st_tekskills.data.model.OpportunityByProjectIDResponse
import com.tekskills.st_tekskills.data.model.ProjectListResponse
import com.tekskills.st_tekskills.data.model.ProjectOpportunityResponse
import com.tekskills.st_tekskills.data.remote.ApiHelper
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiHelper: ApiHelper
) {

    suspend fun userLogin(user: Map<String, String>) = apiHelper.userLogin(user)

    suspend fun userChangePassword(
        authorization: String, password: String) = apiHelper.userChangePassword(authorization, password)

    suspend fun userForgotPassword(userName: String,
    ) = apiHelper.userForgotPassword(userName)

    suspend fun userForgotUsername(email: String,
    ) = apiHelper.userForgotUsername(email)

    suspend fun getEmployeeMe(authorization: String) = apiHelper.getEmployeeMe(authorization)

    suspend fun getEmployeeAllowences(authorization: String, itemID:String)
    = apiHelper.getEmployeeAllowences(authorization, itemID)

    suspend fun addMeetingPurpose(
        authorization: String, user: AddMeetingRequest
    ) = apiHelper.addMeetingPurpose(authorization, user)

    suspend fun addUserCoordinates(
        authorization: String,
        user: AddLocationCoordinates
    ) =apiHelper.addUserCoordinates(authorization, user)

    suspend fun getMeetingPurpose(authorization: String): Response<MeetingPurposeResponse> =
        apiHelper.getMeetingPurpose(authorization)

    suspend fun getMeetingPurposeByID(authorization: String, itemID:String): Response<MeetingPurposeResponseData>
    = apiHelper.getMeetingPurposeByID(authorization, itemID)

    suspend fun getMeetingPurposeByStatus(authorization: String,itemID:String): Response<MeetingPurposeResponse>
    = apiHelper.getMeetingPurposeByStatus(authorization, itemID)

    suspend fun getMeetingPurposeStatus(authorization: String,): Response<MeetingStatusRequest>
    = apiHelper.getMeetingPurposeStatus(authorization)

    suspend fun addTravelExpense(
        authorization: String, purposeID: RequestBody, amount: Long,
        file: MutableList<MultipartBody.Part>?, user: Map<String, RequestBody>
    ): Response<AddTravelExpenceResponse>
    = apiHelper.addTravelExpense(authorization, purposeID, amount,file, user)

    suspend fun addHotelExpense(authorization: String,purposeID: RequestBody,
                                 file: MultipartBody.Part, user: Map<String, RequestBody>
    ): Response<AddTravelExpenceResponse>
            = apiHelper.addHotelExpense(authorization, purposeID,file, user)

    suspend fun addFoodExpense(authorization: String,purposeID: RequestBody,
                               user: Map<String, RequestBody>
    ): Response<AddTravelExpenceResponse>
            = apiHelper.addFoodExpense(authorization, purposeID, user)


    suspend fun addMOMToMeeting(authorization: String,
                                purposeID: RequestBody, file: MutableList<MultipartBody.Part>?,
                                user: Map<String, RequestBody>
    ): Response<AddMOMResponse> = apiHelper.addMOMToMeeting(authorization, purposeID, file, user)

    suspend fun getClients(authorization: String) = apiHelper.getClients(authorization)

    suspend fun getLeads(authorization: String, itemID:String) = apiHelper.getLeads(authorization, itemID)

    suspend fun putUserMeetingCheckIN(
        authorization: String,
        itemID: String,
        requestBody: MutableMap<String, RequestBody>,
        listMultipartImage: MultipartBody.Part
    ) = apiHelper.putUserMeetingCheckIN(authorization, itemID,requestBody,listMultipartImage)

    suspend fun putUserMeetingCheckOUT(authorization: String,itemID:String, requestBody: MutableMap<String, RequestBody>)= apiHelper.putUserMeetingCheckOUT(authorization, itemID,requestBody)

    /**
     * Dashboard Graph items
     */
    suspend fun getPendingActionGraph(authorization: String) =
        apiHelper.getPendingActionGraph(authorization)

    suspend fun getEscalationGraph(authorization: String) =
        apiHelper.getEscalationGraph(authorization)

    suspend fun getPendingActionGraphByID(authorization: String, itemID: String) =
        apiHelper.getPendingActionGraphByID(authorization, itemID)

    suspend fun getEscalationGraphByID(authorization: String, itemID: String) =
        apiHelper.getEscalationGraphByID(authorization, itemID)

    /**
     * ADD Opportunity
     */
    suspend fun addOpportunity(authorization: String, requestBody: AddOpportunityRequest) =
        apiHelper.addOpportunity(authorization, requestBody)




    suspend fun getProjects(authorization: String) = apiHelper.getProjects(authorization)

    suspend fun getAssignProjects(authorization: String) =
        apiHelper.getAssignProjects(authorization)


    suspend fun getManagementList(authorization: String) =
        apiHelper.getManagementList(authorization)

    suspend fun getAccountManagerList(authorization: String) =
        apiHelper.getAccountManagerList(authorization)

    suspend fun getProjectManagerList(authorization: String) =
        apiHelper.getProjectManagerList(authorization)

    suspend fun getPracticeHeadList(authorization: String) =
        apiHelper.getPracticeHeadList(authorization)

    suspend fun getProjectOpportunity(authorization: String): Response<ProjectOpportunityResponse> =
        apiHelper.getProjectOpportunity(authorization)

    suspend fun getMOMProjects(
        authorization: String,
        itemID: String
    ): Response<MomProjectResponse> =
        apiHelper.getMOMProjects(authorization, itemID)

    suspend fun getMOMActionItemDetailsBYId(
        authorization: String,
        itemID: String
    ): Response<MomProjectResponseItem> =
        apiHelper.getMOMActionItemDetailsBYId(authorization, itemID)

    suspend fun getOpportunityByProductID(
        authorization: String,
        itemID: String
    ): Response<OpportunityByProjectIDResponse> =
        apiHelper.getOpportunityByProductID(authorization, itemID)

    suspend fun getClientExist(authorization: String): Response<NewClientResponse> =
        apiHelper.getClientExist(authorization)

    suspend fun getActionItemProjects(authorization: String): Response<NewClientResponse> =
        apiHelper.getActionItemProjects(authorization)

    suspend fun getActionItemProjectID(
        authorization: String,
        itemID: String
    ): Response<ActionItemProjectIDResponse> =
        apiHelper.getActionItemProjectID(authorization, itemID)

    suspend fun getActionItemBYID(
        authorization: String,
        itemID: String
    ): Response<ActionItemProjectIDResponseItem> =
        apiHelper.getActionItemBYID(authorization, itemID)

    suspend fun getProjectID(authorization: String): Response<ProjectListResponse> =
        apiHelper.getProjectID(authorization)

    suspend fun getEscalationItemProjectID(
        authorization: String,
        itemID: String
    ): Response<ClientsEscalationResponse> =
        apiHelper.getEscalationItemProjectID(authorization, itemID)

    suspend fun getCommentProjectID(
        authorization: String,
        itemID: String
    ): Response<CommentsListResponse> =
        apiHelper.getCommentProjectID(authorization, itemID)

    suspend fun addClient(
        authorization: String,
        user: Map<String, String>
    ) = apiHelper.addClient(authorization, user)

    suspend fun addProject(authorization: String, user: Map<String, String>) =
        apiHelper.addProject(authorization, user)

    suspend fun addEmployee(authorization: String, user: Map<String, String>) =
        apiHelper.addEmployee(authorization, user)

    suspend fun assignProject(authorization: String, user: Map<String, String>) =
        apiHelper.assignProject(authorization, user)

    suspend fun addClientEscalation(authorization: String, user: AddEscalationRequest) =
        apiHelper.addClientEscalation(authorization, user)

    suspend fun addMOMActionItem(authorization: String, user: AddMOMOpportunityRequest) =
        apiHelper.addMOMActionItem(authorization, user)

    suspend fun addActionItemOpportunity(
        authorization: String,
        user: AddActionItemOpportunityRequest
    ) = apiHelper.addActionItemOpportunity(authorization, user)


    suspend fun addCommentOpportunity(
        authorization: String,
        user: AddCommentOpportunity
    ) = apiHelper.addCommentOpportunity(authorization, user)

   suspend fun uploadAudioFile(
        authorization: String,
        purposeId: RequestBody,
        callForm: RequestBody,
        callTo: RequestBody,
        callType: RequestBody,
        startAt: RequestBody,
        file: MultipartBody.Part
    ) = apiHelper.uploadAudioFile(authorization, purposeId, callForm, callTo, callType, startAt, file)
}