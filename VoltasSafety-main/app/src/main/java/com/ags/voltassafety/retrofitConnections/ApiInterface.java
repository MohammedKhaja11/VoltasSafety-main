package com.ags.voltassafety.retrofitConnections;

import com.ags.voltassafety.model.ActionItemReOpenInput;
import com.ags.voltassafety.model.ActionRemarksModel;
import com.ags.voltassafety.model.AttachmentDeleteResponse;
import com.ags.voltassafety.model.AttachmentUpload;
import com.ags.voltassafety.model.AttachmentUploadModel;
import com.ags.voltassafety.model.ChangePassword;
import com.ags.voltassafety.model.ChangePasswordModel;
import com.ags.voltassafety.model.ConfirmPassword;
import com.ags.voltassafety.model.ConfirmPasswordResponse;
import com.ags.voltassafety.model.CreateManpowerInput;
import com.ags.voltassafety.model.CreateResponse;
import com.ags.voltassafety.model.CustomerResponse;
import com.ags.voltassafety.model.EmailLoginOTPVerificationResponse;
import com.ags.voltassafety.model.EmailSignInResponse;
import com.ags.voltassafety.model.EmailandOtpVerificationRequest;
import com.ags.voltassafety.model.EmainSignInRequest;
import com.ags.voltassafety.model.EntityResponse;
import com.ags.voltassafety.model.EscalationModel;
import com.ags.voltassafety.model.EscalationResponse;
import com.ags.voltassafety.model.FileResponse;
import com.ags.voltassafety.model.ForgorPasswordResponse;
import com.ags.voltassafety.model.ManPowerResponse;
import com.ags.voltassafety.model.ObservationReportDetailsInput;
import com.ags.voltassafety.model.HazardBranchModel;
import com.ags.voltassafety.model.HazardBranchObservationStatusResponse;
import com.ags.voltassafety.model.HazardBranchResponse;
import com.ags.voltassafety.model.HazardStatusWiseReportResponse;
import com.ags.voltassafety.model.LoginCredentials;
import com.ags.voltassafety.model.LoginResponse;
import com.ags.voltassafety.model.NotificationModel;
import com.ags.voltassafety.model.NotificationResponse;
import com.ags.voltassafety.model.ObservationClosingModel;
import com.ags.voltassafety.model.ObservationCountResponse;
import com.ags.voltassafety.model.ObservationDetailsResponse;
import com.ags.voltassafety.model.ObservationHeader;
import com.ags.voltassafety.model.ObservationListViewResponse;
import com.ags.voltassafety.model.ObservationReportDetailsResponse;
import com.ags.voltassafety.model.ObservationReportInput;
import com.ags.voltassafety.model.ObservationReportResponse;
import com.ags.voltassafety.model.ObservationReportSummaryResponse;
import com.ags.voltassafety.model.ObservationResponse;
import com.ags.voltassafety.model.ObservationStatusReportResult;
import com.ags.voltassafety.model.RegisterInputModel;
import com.ags.voltassafety.model.SendOtpResponse;
import com.ags.voltassafety.model.SiteEngineerForEmployeeResponse;
import com.ags.voltassafety.model.SiteEnginnerResponse;
import com.ags.voltassafety.model.UserBranchResponse;
import com.ags.voltassafety.model.UserProfileResponse;
import com.ags.voltassafety.model.UserZoneResponse;
import com.ags.voltassafety.model.VersionUpdateModel;
import com.ags.voltassafety.model.Verticleresponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {

    @Headers({"Accept: application/json"})
    @POST("User/signin")
    Call<LoginResponse> validateUser(@Body LoginCredentials data);

    @Headers({"Accept: application/json"})
    @GET("User/ForgotPassword/{user}")
    Call<ForgorPasswordResponse> getForgotPassword(@Path("user") String userId);

    @Headers({"Accept: application/json"})
    @GET("User/EmployeeCreateRequest/{reportEnggMail}/{empId}/{empName}")
    Call<CreateResponse> setEmailRequestForUserCreate(@Path("reportEnggMail") String reportEnggMail, @Path("empId") String empId, @Path("empName") String empName);

    @Headers({"Accept: application/json"})
    @GET("User/EmployeeResetPasswordRequest/{reportEnggMail}/{empMail}/{empId}/{empName}")
    Call<CreateResponse> setEmailRequestForPwdReset(@Path("reportEnggMail") String reportEnggMail, @Path("empMail") String empMail, @Path("empId") String empId, @Path("empName") String empName);

    @Headers({"Accept: application/json"})
    @POST("User/ResetPassword")
    Call<ConfirmPasswordResponse> setResetPassword(@Body ConfirmPassword data);

    @Headers({"Accept: application/json"})
    @GET("ApplicationVersions/appupdateavailability/{appCode}/{oSType}")
    Call<VersionUpdateModel> getUpdateAppVersion(@Path("appCode") String appCode, @Path("oSType") String oSType);

    @GET
    Call<ResponseBody> downloadFileWithDynamicUrl(@Url String fileUrl);

    @Headers({"Accept: application/json"})
    @POST("Attachment/ObservationAttachmentsCreate")
    Call<FileResponse> uploadObservationAttachment(@Header("Authorization") String Response, @Body AttachmentUpload data);

    @Headers({"Accept: application/json"})
    @POST("Attachment/ObservationAttachmentsCreate")
    Call<FileResponse> uploadCameraAttachment(@Header("Authorization") String Response, @Body AttachmentUploadModel data);

    @Headers({"Accept: application/json"})
    @POST("User/ChangePassword")
    Call<ChangePasswordModel> changePassword(@Header("Authorization") String Response, @Body ChangePassword data);

    @Headers({"Accept: application/json"})
    @GET("LookUp/GetByEntityName/{entityName}")
    Call<EntityResponse> getEntityMapData(@Header("Authorization") String Response, @Path("entityName") String userId);

    @Headers({"Accept: application/json"})
    @GET("Customer/GetUserCustomers")
    Call<CustomerResponse> getUserCustomers(@Header("Authorization") String Response);

    @Headers({"Accept: application/json"})
    @GET("User/GetAllUsers")
    Call<SiteEnginnerResponse> getSiteEngineers(@Header("Authorization") String Response);


    @Headers({"Accept: application/json"})
    @GET("User/SearchUsers/{searchKey}")
    Call<SiteEnginnerResponse> getSiteEngineers(@Header("Authorization") String Response, @Path("searchKey") String searchKey);

    @Headers({"Accept: application/json"})
    @POST("Observation/Create")
    Call<CreateResponse> createObservation(@Header("Authorization") String Response, @Body ObservationHeader data);


    @Headers({"Accept: application/json"})
    @POST("User/DeviceRegistration")
    Call<NotificationResponse> pushNotification(@Header("Authorization") String Response, @Body NotificationModel data);


    @Headers({"Accept: application/json"})
    @GET("Observation/GetAllClients")
    Call<Verticleresponse> getVerticleData(@Header("Authorization") String Response);

    @Headers({"Accept: application/json"})
    @GET("Observation/GetUserObservations/{type}/{subtype}")
    Call<ObservationResponse> getObservationsByType(@Header("Authorization") String Response, @Path("type") String type, @Path("subtype") String subtype);

    @Headers({"Accept: application/json"})
    @GET("Observation/GetUserObservationsSummary")
    Call<ObservationCountResponse> getObservationSummary(@Header("Authorization") String Response);

    @Headers({"Accept: application/json"})
    @POST("ObservationReport/GetBranchsObservationsReport")
    Call<HazardBranchResponse> getBranchObservationReport(@Header("Authorization") String Response, @Body HazardBranchModel hazardBranchModel);

    @Headers({"Accept: application/json"})
    @POST("ObservationReport/GetBranchObservationStatusReport")
    Call<HazardBranchObservationStatusResponse> getBranchObservationStatusReport(@Header("Authorization") String Response, @Body HazardBranchModel hazardBranchModel);


    @Headers({"Accept: application/json"})
    @POST("ObservationReport/GetBranchObservationStatusReportDetails")
    Call<HazardStatusWiseReportResponse> getBranchObservationStatusReportDetails(@Header("Authorization") String Response, @Body HazardBranchModel hazardBranchModel);

    @Headers({"Accept: application/json"})
    @GET("ObservationReport/GetObservationsReportSummary")
    Call<ObservationReportSummaryResponse> getObservationsReportSummary(@Header("Authorization") String Response);

    @Headers({"Accept: application/json"})
    @GET("Observation/GetUserObservations/{searchKey}/{type}/{subtype}")
    Call<ObservationResponse> searchObservations(@Header("Authorization") String Response, @Path("searchKey") String searchKey, @Path("type") String type, @Path("subtype") String subtype);

    @Headers({"Accept: application/json"})
    @GET("Observation/GetUserObservations")
    Call<ObservationResponse> getAllObservations(@Header("Authorization") String Response);

    @Headers({"Accept: application/json"})
    @GET("ObservationReport/GetObservationPartiallyclosedReportDetails")
    Call<ObservationResponse> getPartialObservations(@Header("Authorization") String Response);


    @Headers({"Accept: application/json"})
    @GET("Observation/GetById/{observationId}")
    Call<ObservationDetailsResponse> getObservationByID(@Header("Authorization") String Response, @Path("observationId") String observationId);

    @Headers({"Accept: application/json"})
    @PUT("Observation/Update")
    Call<CreateResponse> updateObservation(@Header("Authorization") String Response, @Body ObservationHeader data);

    @Headers({"Accept: application/json"})
    @PUT("Observation/ReopenItemAction")
    Call<CreateResponse> reopenObservationItem(@Header("Authorization") String Response, @Body ActionItemReOpenInput data);

    @Headers({"Accept: application/json"})
    @DELETE("Observation/DeleteObservationItem/{observationItemId}")
    Call<CreateResponse> deleteObservationItem(@Header("Authorization") String Response, @Path("observationItemId") int observationItemId);

    @Headers({"Accept: application/json"})
    @PUT("Observation/CloseObservation")
    Call<CreateResponse> closeObservation(@Header("Authorization") String Response, @Body ObservationClosingModel observationClosingModel);

    @Headers({"Accept: application/json"})
    @PUT("Observation/CloseItemAction")
    Call<CreateResponse> closeActionItem(@Header("Authorization") String Response, @Body ActionRemarksModel actionRemarksModel);


    @Headers({"Accept: application/json"})
    @GET("Report/GetUserBranchs")
    Call<UserBranchResponse> getUserBranchResponse(@Header("Authorization") String Response);

    @Headers({"Accept: application/json"})
    @GET("Report/GetUserZones")
    Call<UserZoneResponse> getUserZoneResponse(@Header("Authorization") String Response);

    @Headers({"Accept: application/json"})
    @POST("ObservationReport/GetUserObservationReport")
    Call<ObservationReportResponse> observationReportResponse(@Header("Authorization") String Response, @Body ObservationReportInput data);


    @Headers({"Accept: application/json"})
    @POST("ObservationReport/GetUserObservationReportDetails")
    Call<ObservationListViewResponse> observationListviewResponse(@Header("Authorization") String Response, @Body ObservationReportInput data);

    @Headers({"Accept: application/json"})
    @POST("ObservationReport/GetObservationEscalationReport")
    Call<EscalationResponse> observationescalationResponse(@Header("Authorization") String Response, @Body EscalationModel data);


    @Headers({"Accept: application/json"})
    @GET("Report/GetGuestZones/{country}")
    Call<UserZoneResponse> getGuestZoneResponse(@Header("Authorization") String Response, @Path("country") String country);


    @Headers({"Accept: application/json"})
    @GET("Report/GetGuestBranchs/{zoneId}")
    Call<UserBranchResponse> getGuestBranchResponse(@Header("Authorization") String Response, @Path("zoneId") String zoneId);


    @Headers({"Accept: application/json"})
    @GET("Report/GetZones")
    Call<UserZoneResponse> getZoneDetails(@Header("Authorization") String Response);

    @Headers({"Accept: application/json"})
    @GET("Report/GetObservationsReportSummary")
    Call<UserZoneResponse> getObservationsReportSummaryDetail(@Header("Authorization") String Response);

    //    @Headers({"Accept: application/json"})
//    @GET(" Report/GetBranchs")
//    Call<UserBranchResponse> getBranchDetails(@Header("Authorization") String Response);
    @Headers({"Accept: application/json"})
    @POST("Report/GetBranchs")
    Call<UserBranchResponse> getBranchDetails(@Header("Authorization") String Response, @Body String[] data);

    @Headers({"Accept: application/json"})
    @POST("ObservationReport/GetObservationReportDetails")
    Call<ObservationReportDetailsResponse> GetObservationReportDetails(@Header("Authorization") String Response, @Body ObservationReportDetailsInput data);


    @Headers({"Accept: application/json"})
    @POST("ObservationReport/GetObservationStatusReport")
    Call<ObservationStatusReportResult> GetObservationStatusReport(@Header("Authorization") String Response, @Body ObservationReportDetailsInput data);


    @Headers({"Accept: application/json"})
    @DELETE("Observation/DeleteObservationAttachments/{attachmentId}")
    Call<AttachmentDeleteResponse> deleteattachment(@Header("Authorization") String Response, @Path("attachmentId") int attachmentId);


    @Headers({"Accept: application/json"})
    @GET("User/EmailloginOtpSendMethod/{EmailID}")
    Call<SendOtpResponse> getOtp(@Path("EmailID") String observationId);


    @Headers({"Accept: application/json"})
    @POST("User/EmailloginOtpVerificationMethod")
    Call<EmailLoginOTPVerificationResponse> getEmailandOtpVerification(@Body EmailandOtpVerificationRequest emailandOtpRequest);


    @Headers({"Accept: application/json"})
    @POST("User/EmailSignIn")
    Call<EmailSignInResponse> getMailSignIN(@Body EmainSignInRequest emainSignInRequest);


    @Headers({"Accept: application/json"})
    @GET("ObservationReport/GetObservationReportDetailsByEmailID/{EmailID}")
    Call<ObservationResponse> getObservationbyEmailID(@Header("Authorization") String Response,@Path("EmailID") String emailid, @Query("TypeOfObservation") String TypeOfObservation);


    @Headers({"Accept: application/json"})
    @GET("ObservationReport/GetEmployeDetailsByEmailID/{EmailID}")
    Call<SiteEngineerForEmployeeResponse> getSiteEngineersforEmployee(@Header("Authorization") String Response,@Path("EmailID") String searchKey);

    @Headers({"Accept: application/json"})
    @GET("Registration/IsRegistrationExist/{Type}")
    Call<CreateResponse> getIsRegisterExist(@Header("Authorization") String Response,@Path("Type") String searchKey);

    @Headers({"Accept: application/json"})
    @GET("Registration//GetUserRegistrationStatus/{Type}")
    Call<CreateResponse> getEmployeeRegistrationStatus(@Header("Authorization") String Response,@Path("Type") String Type);

    @Headers({"Accept: application/json"})
    @POST("Registration/CreateRegistration")
    Call<CreateResponse> createRegistration(@Header("Authorization") String Response,@Body RegisterInputModel registerInputModel);

    @Headers({"Accept: application/json"})
    @PUT("Registration/UpdateRegistration")
    Call<String> updateRegistration(@Header("Authorization") String Response,@Body RegisterInputModel registerInputModel);

    @Headers({"Accept: application/json"})
    @GET("ManPower/GetUserCurentMonthManPower")
    Call<ManPowerResponse> getManPower(@Header("Authorization") String Response);

    @Headers({"Accept: application/json"})
    @GET("User/getuserprofile")
    Call<UserProfileResponse> getUserProfile(@Header("Authorization") String Response);

    @Headers({"Accept: application/json"})
    @POST("ManPower/CreateManPower")
    Call<CreateResponse> createManPower(@Header("Authorization") String Response,@Body CreateManpowerInput createManpowerInput);

    @Headers({"Accept: application/json"})
    @PUT("ManPower/UpdateManPower/{ManPowerID}")
    Call<CreateResponse> updateManPower(@Header("Authorization") String Response,@Body CreateManpowerInput createManpowerInput,@Path("ManPowerID") String ManPowerID);
}
