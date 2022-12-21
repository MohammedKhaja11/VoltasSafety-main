package com.ags.voltassafety;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.ags.voltassafety.model.CreateManpowerInput;
import com.ags.voltassafety.model.CreateResponse;
import com.ags.voltassafety.model.ManPowerResponse;
import com.ags.voltassafety.model.RegisterInputModel;
import com.ags.voltassafety.model.UserBranchResponse;
import com.ags.voltassafety.model.UserZoneResponse;
import com.ags.voltassafety.model.Verticleresponse;
import com.ags.voltassafety.retrofitConnections.ApiInterface;
import com.ags.voltassafety.retrofitConnections.RetrofitConnect;
import com.ags.voltassafety.utility.BaseActivity;
import com.ags.voltassafety.utility.Utilities;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateManpowerActivity extends BaseActivity {

    EditText mETSiteProjectName, mETDate, mETProjectManagerName, mETProjectManagerMail, mETHeadCount, totalManHoursCount, mETHeadCountContract,
            mETManHoursWorkedContract, mETManHoursWorked, mETProjectManagerMobileno, mETSafetyOfficerName,
            mETSafetyOfficerMail, mETSafetyOfficerMobileno, mETPDBUName, mETPDBUMail, mETPDBUMobileno,
            mETNoOfParticipents, mETNoOFSafetyMeetings, mETNoOfUnsafeActs, mETNoOfUnsafeConditions, mETFireExtinguishers, mETELCB,
            mETElectrical, mETFullBodyHarness, mETPPES, mETLiftingEquipment, mETGrindingMachine, mETWeldingMachine, mETTotal,
            mETThirdPartyVehicle, mETScaffoldLadder, mETOfficeStore, mETGasCuttingMachine, mETEmergencyTotal, mETMockDrillDate,
            mETFireDrillCount, mETFireDrillDate, mETMockDrillCount, mETElectricalFire, mETSlipTrip, mETFallOfMaterial, mETEquipment,
            mETCollision, mETToppling, mETFloorOpening, mETVehicleMovement, mETNearmissTotal, mETNSD, mETRoad, mETWED, mETHealth, mETOther,
            mETSafetyEventsTotal, mETSrLeadership, mETInternal, mETExternal, mETAuditTotal, mETSrManagement, mETBUHeads, mETSLLB,
            mETNoOfRewardGiven, mETNoOfSafetyViolationMemo, mETSafetyCommTotal, mETStructureCollapse, mETHighPressureRelease,
            mETHighLowTempExposure, mETDangerousTotal, mETFatal, mETLTI, mETMTI, mETFirstAid, mETInjuriesTotal,
            mETLTIFR, mETLTIFRPercent, mETMTIFR, mETMTIFRPercent, mETAIFR, mETAIFRPercent, mETInjuriesRateTotal, mETBurstingMachine,
            mETPPE, mETWorkAtHeight, mETEnvironment, mETLowRisk, mETMediumRisk, mETHighRisk, mETCloseObservation, mETOpenObservation,
            mETObservationTotal, mETExternalOtherName, mETInternalOtherName;

    TextView mTVVertical, mTVZone, mTVBranch, mCancel, mNext, mTitle, mTVIncidentInformation, mTVProjectManagerInformation,
            mTVSafetyOfficer, mTVEditPDBU, mTVNoSafetyInspections, mTVEmergencyResponse, mTVNearMiss, mTVSafetyEvents, mTVNoOfAudits,
            mTVSafetyCommunications, mTVDangerousOccurences, mTVNoOfInjuries, mTVInjuryRate, mTVObservationEmp, mTVInternalExternalName;

    Spinner mSPVertical, mSPZone, mSPBranch;
    ArrayList<String> zonesArrayList, verticleArrayList;
    ArrayList<String> zonesArrayListId;
    ArrayList<String> branchArrayList;
    ArrayList<String> branchArrayListId;
    ArrayAdapter zoneAdapter, branchArrayAdapter, verticalAdapter;
    private String zoneID, branchID;
    CreateManpowerInput objManpowerInput;
    String strSelectedDate = null;
    int intTotal = 0;
    SimpleDateFormat mDateSDF, mDateOutput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_create_manpower);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            objManpowerInput = new CreateManpowerInput();

            mDateSDF = new SimpleDateFormat("dd-MM-yyyy");
            mDateOutput = new SimpleDateFormat("yyyy-MM-dd'T'");

            mETSiteProjectName = findViewById(R.id.edit_site_project_name);
            mTVVertical = findViewById(R.id.textVertical);
            mSPVertical = findViewById(R.id.spinnerVertical);
            mTVZone = findViewById(R.id.textZone);
            mSPZone = findViewById(R.id.spinnerZone);
            mTVBranch = findViewById(R.id.textBranch);
            mSPBranch = findViewById(R.id.spinnerBranch);
            mETDate = findViewById(R.id.edit_date);

            mTVIncidentInformation = findViewById(R.id.incident_information_text);
            mETHeadCount = findViewById(R.id.edit_head_count);
            totalManHoursCount = findViewById(R.id.edit_total_man_hours_worked);
            mETHeadCountContract = findViewById(R.id.edit_head_count_contract);
            mETManHoursWorked = findViewById(R.id.edit_man_hours_worked);
            mETManHoursWorkedContract = findViewById(R.id.edit_man_hours_worked_contract);

            mTVObservationEmp = findViewById(R.id.txt_no_of_suggestions);
            mETPPE = findViewById(R.id.edit_ppe);
            mETWorkAtHeight = findViewById(R.id.edit_workatheight);
            mETEnvironment = findViewById(R.id.edit_environment);
            mETLowRisk = findViewById(R.id.edit_lowrisk);
            mETMediumRisk = findViewById(R.id.edit_mediumrisk);
            mETHighRisk = findViewById(R.id.edit_highrisk);
            mETCloseObservation = findViewById(R.id.edit_closeobservation);
            mETOpenObservation = findViewById(R.id.edit_openobservation);
            mETObservationTotal = findViewById(R.id.edit_obserationtotal);

            mTVInternalExternalName = findViewById(R.id.txt_other_internal_external_name);
            mETInternalOtherName = findViewById(R.id.edit_internal_other_name);
            mETExternalOtherName = findViewById(R.id.edit_external_other_name);

            mTVProjectManagerInformation = findViewById(R.id.project_manager_information_text);
            mETProjectManagerName = findViewById(R.id.edit_project_manager_name);
            mETProjectManagerMail = findViewById(R.id.edit_project_manager_mail);
            mETProjectManagerMobileno = findViewById(R.id.edit_project_manager_mobileno);

            mTVSafetyOfficer = findViewById(R.id.txt_safety_officer);
            mETSafetyOfficerName = findViewById(R.id.edit_safety_officer_name);
            mETSafetyOfficerMail = findViewById(R.id.edit_safety_officer_mail);
            mETSafetyOfficerMobileno = findViewById(R.id.edit_safety_officer_mobileno);

            mTVEditPDBU = findViewById(R.id.text_pb_bu);
            mETPDBUName = findViewById(R.id.edit_pb_bu_name);
            mETPDBUMail = findViewById(R.id.edit_pb_bu_mail);
            mETPDBUMobileno = findViewById(R.id.edit_pb_bu_mobileno);

            mETNoOfParticipents = findViewById(R.id.edit_no_of_participents);
            mETNoOFSafetyMeetings = findViewById(R.id.edit_no_of_safetymeetings);
            mETNoOfUnsafeActs = findViewById(R.id.edit_no_of_unsafe_acts);
            mETNoOfUnsafeConditions = findViewById(R.id.edit_no_of_unsafe_conditions);
            //mETNoOfSheMeetings = findViewById(R.id.edit_no_of_she_meetings);

            mTVNoSafetyInspections = findViewById(R.id.no_safety_inspections);
            mETFireExtinguishers = findViewById(R.id.edit_fire_extinguishers);
            mETELCB = findViewById(R.id.edit_elcb);
            mETElectrical = findViewById(R.id.edit_electrical);
            mETFullBodyHarness = findViewById(R.id.edit_full_body_harness);
            mETPPES = findViewById(R.id.edit_ppes);
            mETLiftingEquipment = findViewById(R.id.edit_lifting_equipment);
            mETGrindingMachine = findViewById(R.id.edit_grinding_machine);
            mETWeldingMachine = findViewById(R.id.edit_welding_machine);
            mETGasCuttingMachine = findViewById(R.id.edit_gas_cutting_machine);
            mETOfficeStore = findViewById(R.id.edit_office_store);
            mETScaffoldLadder = findViewById(R.id.edit_scaffold_ladder);
            mETThirdPartyVehicle = findViewById(R.id.edit_third_party_vehicle);
            mETTotal = findViewById(R.id.edit_total);

            mTVEmergencyResponse = findViewById(R.id.txt_emergency_response);
            mETFireDrillCount = findViewById(R.id.edit_fire_drill_count);
            mETFireDrillDate = findViewById(R.id.edit_fire_drill_date);
            mETMockDrillCount = findViewById(R.id.edit_mock_drill_count);
            mETMockDrillDate = findViewById(R.id.edit_mock_drill_date);
            mETEmergencyTotal = findViewById(R.id.edit_emergencytotal);

            mTVNearMiss = findViewById(R.id.text_nearmiss);
            mETElectricalFire = findViewById(R.id.edit_electrical_fire);
            mETSlipTrip = findViewById(R.id.edit_slip_trip);
            mETFallOfMaterial = findViewById(R.id.edit_fall_of_material);
            mETEquipment = findViewById(R.id.edit_equipment);
            mETCollision = findViewById(R.id.edit_collision);
            mETToppling = findViewById(R.id.edit_toppling);
            mETFloorOpening = findViewById(R.id.edit_floor_opening);
            mETVehicleMovement = findViewById(R.id.edit_vehicle_movement);
            mETNearmissTotal = findViewById(R.id.edit_nearmiss_total);

            mTVSafetyEvents = findViewById(R.id.text_safety_events);
            mETNSD = findViewById(R.id.edit_nsd);
            mETRoad = findViewById(R.id.edit_road);
            mETWED = findViewById(R.id.edit_wed);
            mETHealth = findViewById(R.id.edit_health);
            mETOther = findViewById(R.id.edit_other);
            mETSafetyEventsTotal = findViewById(R.id.edit_safety_events_total);

            mTVNoOfAudits = findViewById(R.id.text_no_of_audits);
            mETSrLeadership = findViewById(R.id.edit_sr_leadership);
            mETInternal = findViewById(R.id.edit_internal);
            mETExternal = findViewById(R.id.edit_external);
            mETAuditTotal = findViewById(R.id.edit_audits_total);

            mTVSafetyCommunications = findViewById(R.id.text_safety_communications);
            mETSrManagement = findViewById(R.id.edit_sr_management);
            mETBUHeads = findViewById(R.id.edit_bu_heads);
            mETSLLB = findViewById(R.id.edit_sllb);
            mETNoOfRewardGiven = findViewById(R.id.edit_no_of_reward_given);
            mETNoOfSafetyViolationMemo = findViewById(R.id.edit__no_of_safety_violation_memo);
            mETSafetyCommTotal = findViewById(R.id.edit_safety_comm_total);

            mTVDangerousOccurences = findViewById(R.id.text_dangerous_occurences);
            mETStructureCollapse = findViewById(R.id.edit_structure_collapse);
            mETHighPressureRelease = findViewById(R.id.edit_high_pressure_release);
            mETHighLowTempExposure = findViewById(R.id.edit_high_low_temp_exposure);
            mETBurstingMachine = findViewById(R.id.edit_bursting_machine);
            mETDangerousTotal = findViewById(R.id.edit_dangerous_total);

            mTVNoOfInjuries = findViewById(R.id.text_no_of_injuries);
            mETFatal = findViewById(R.id.edit_fatal);
            mETLTI = findViewById(R.id.edit_lti);
            mETMTI = findViewById(R.id.edit_mti);
            mETFirstAid = findViewById(R.id.edit_first_aid);
            mETInjuriesTotal = findViewById(R.id.edit_injuries_total);

            mTVInjuryRate = findViewById(R.id.text_injury_rate);
            mETLTIFR = findViewById(R.id.edit_ltifr);
            mETLTIFRPercent = findViewById(R.id.edit_ltifr_percent);
            mETMTIFR = findViewById(R.id.edit_mtifr);
            mETMTIFRPercent = findViewById(R.id.edit_mtifr_percent);
            mETAIFR = findViewById(R.id.edit_aifr);
            mETAIFRPercent = findViewById(R.id.edit_aifr_percent);
            mETInjuriesRateTotal = findViewById(R.id.edit_injuries_rate_total);

            mCancel = findViewById(R.id.cancel);
            mNext = findViewById(R.id.next);
            mTitle = findViewById(R.id.create_title);

            mTitle.setTypeface(Utilities.fontBold(getApplicationContext()));
            mCancel.setTypeface(Utilities.fontRegular(getApplicationContext()));
            mNext.setTypeface(Utilities.fontRegular(getApplicationContext()));
            totalManHoursCount.setTypeface(Utilities.fontRegular(getApplicationContext()));

            mETSiteProjectName.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mTVVertical.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mTVZone.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mTVBranch.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETDate.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));

            mTVIncidentInformation.setTypeface(Utilities.fontBold(CreateManpowerActivity.this));
            mETHeadCount.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETHeadCountContract.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETManHoursWorked.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETManHoursWorkedContract.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));

            mTVObservationEmp.setTypeface(Utilities.fontBold(CreateManpowerActivity.this));
            mETPPE.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETWorkAtHeight.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETEnvironment.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETLowRisk.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETMediumRisk.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETHighRisk.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETCloseObservation.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETOpenObservation.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETObservationTotal.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));

            mTVInternalExternalName.setTypeface(Utilities.fontBold(CreateManpowerActivity.this));
            mETInternalOtherName.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETExternalOtherName.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));

            mTVProjectManagerInformation.setTypeface(Utilities.fontBold(CreateManpowerActivity.this));
            mETProjectManagerName.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETProjectManagerMail.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETProjectManagerMobileno.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));

            mTVSafetyOfficer.setTypeface(Utilities.fontBold(CreateManpowerActivity.this));
            mETSafetyOfficerName.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETSafetyOfficerMail.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETSafetyOfficerMobileno.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));

            mTVEditPDBU.setTypeface(Utilities.fontBold(CreateManpowerActivity.this));
            mETPDBUName.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETPDBUMail.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETPDBUMobileno.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));

            mETNoOfParticipents.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETNoOFSafetyMeetings.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETNoOfUnsafeActs.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETNoOfUnsafeConditions.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            //mETNoOfSheMeetings.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));

            mTVNoSafetyInspections.setTypeface(Utilities.fontBold(CreateManpowerActivity.this));
            mETFireExtinguishers.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETELCB.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETElectrical.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETFullBodyHarness.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETPPES.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETLiftingEquipment.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETGrindingMachine.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETWeldingMachine.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETGasCuttingMachine.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETOfficeStore.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETScaffoldLadder.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETThirdPartyVehicle.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETTotal.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));

            mTVEmergencyResponse.setTypeface(Utilities.fontBold(CreateManpowerActivity.this));
            mETFireDrillCount.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETFireDrillDate.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETMockDrillCount.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETMockDrillDate.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETEmergencyTotal.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));

            mTVNearMiss.setTypeface(Utilities.fontBold(CreateManpowerActivity.this));
            mETElectricalFire.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETSlipTrip.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETFallOfMaterial.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETEquipment.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETCollision.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETToppling.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETFloorOpening.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETVehicleMovement.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETNearmissTotal.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));

            mTVSafetyEvents.setTypeface(Utilities.fontBold(CreateManpowerActivity.this));
            mETNSD.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETRoad.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETWED.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETHealth.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETOther.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETSafetyEventsTotal.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));

            mTVNoOfAudits.setTypeface(Utilities.fontBold(CreateManpowerActivity.this));
            mETSrLeadership.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETInternal.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETExternal.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETAuditTotal.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));

            mTVSafetyCommunications.setTypeface(Utilities.fontBold(CreateManpowerActivity.this));
            mETSrManagement.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETBUHeads.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETSLLB.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETNoOfRewardGiven.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETNoOfSafetyViolationMemo.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETSafetyCommTotal.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));

            mTVDangerousOccurences.setTypeface(Utilities.fontBold(CreateManpowerActivity.this));
            mETStructureCollapse.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETHighPressureRelease.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETHighLowTempExposure.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            //mTVBurstingMachine.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETDangerousTotal.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));

            mTVNoOfInjuries.setTypeface(Utilities.fontBold(CreateManpowerActivity.this));
            mETFatal.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETLTI.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETMTI.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETFirstAid.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETInjuriesTotal.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));

            mTVInjuryRate.setTypeface(Utilities.fontBold(CreateManpowerActivity.this));
            mETLTIFR.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETLTIFRPercent.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETMTIFR.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETMTIFRPercent.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETAIFR.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETAIFRPercent.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));
            mETInjuriesRateTotal.setTypeface(Utilities.fontRegular(CreateManpowerActivity.this));

            if (Utilities.isConnectingToInternet(CreateManpowerActivity.this)) {

                getManPowerDetails();
            } else {
                showAlert("Please Check Your Internet Connection");
            }
            mETManHoursWorked.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    int count = 0;
                    int countOne = 0;
                    try {
                        if (mETManHoursWorked.getText().toString().length() > 0) {
                            count = Integer.parseInt(mETManHoursWorked.getText().toString().trim());
                        }
                        if (mETManHoursWorkedContract.getText().toString().length() > 0) {
                            countOne = Integer.parseInt(mETManHoursWorkedContract.getText().toString().trim());
                        }
                        int total = count + countOne;
                        totalManHoursCount.setText(total + "");
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            });
            mETManHoursWorkedContract.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    int count = 0;
                    int countOne = 0;
                    try {
                        if (mETManHoursWorked.getText().toString().length() > 0) {
                            count = Integer.parseInt(mETManHoursWorked.getText().toString().trim());
                        }
                        if (mETManHoursWorkedContract.getText().toString().length() > 0) {
                            countOne = Integer.parseInt(mETManHoursWorkedContract.getText().toString().trim());
                        }
                        int total = count + countOne;
                        totalManHoursCount.setText(total + "");
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            });
            mETHeadCountContract.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    int count = 0;
                    try {
                        if (mETHeadCountContract.getText().toString().length() > 0) {
                            count = Integer.parseInt(mETHeadCountContract.getText().toString().trim());
                        }
                        int total = count * 8 * 26;
                        mETManHoursWorkedContract.setText(total + "");
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            });
            mETHeadCount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    int count = 0;
                    try {
                        if (mETHeadCount.getText().toString().length() > 0) {
                            count = Integer.parseInt(mETHeadCount.getText().toString().trim());
                        }
                        int total = count * 8 * 26;
                        mETManHoursWorked.setText(total + "");
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            });


            mETDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
            mETFireDrillDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
            mETMockDrillDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
            //mETDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
            mETFireDrillDate.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    mETFireDrillDate.setFocusableInTouchMode(false);
                    mETFireDrillDate.setFocusable(false);
                    mETFireDrillDate.setError(null);
                    final Dialog calenderdialog = new Dialog(CreateManpowerActivity.this, R.style.MyDialogTheme);
                    calenderdialog.setContentView(R.layout.activity_incidentdate);
                    calenderdialog.setCanceledOnTouchOutside(false);
                    calenderdialog.show();
                    CalendarView calendarView;
                    calendarView = calenderdialog.findViewById(R.id.calendarView);
                    TextView cancel = calenderdialog.findViewById(R.id.cancel);
                    TextView done = calenderdialog.findViewById(R.id.done);
                    calendarView.setMaxDate(new Date().getTime());
                    strSelectedDate = null;
                    calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                        @Override
                        public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                            strSelectedDate = null;
                            int m = month + 1;
                            String currentmonth;
                            String currentday;
                            if (m < 10) {
                                currentmonth = "0" + m + "";
                            } else {
                                currentmonth = m + "";
                            }
                            if (dayOfMonth < 10) {
                                currentday = "0" + dayOfMonth;
                            } else {
                                currentday = dayOfMonth + "";
                            }
                            strSelectedDate = currentday + "-" + currentmonth + "-" + year;
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            calenderdialog.dismiss();
                        }
                    });

                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (strSelectedDate != null) {

                                mETFireDrillDate.setText(strSelectedDate);

                            } /*else {
                                mETDate.setText(part1);
                            }
                          Date selectedDate = null;
                            try {
                                selectedDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(mETDate.getText().toString() + " " + new SimpleDateFormat("HH:mm").format(new Date()) + ":00");
                            } catch (ParseException p) {
                                p.getMessage();
                            }

                            if (selectedDate.equals(new Date()) || selectedDate.before(new Date())) {
                                mETDate.setText(new SimpleDateFormat("HH:mm").format(selectedDate));
                            } else {
                                mETDate.setText(new SimpleDateFormat("HH:mm").format(new Date()));
                            }*/
                            calenderdialog.dismiss();
                        }
                    });
                }
            });

            mETMockDrillDate.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    mETMockDrillDate.setFocusableInTouchMode(false);
                    mETMockDrillDate.setFocusable(false);
                    mETMockDrillDate.setError(null);
                    final Dialog calenderdialog = new Dialog(CreateManpowerActivity.this, R.style.MyDialogTheme);
                    calenderdialog.setContentView(R.layout.activity_incidentdate);
                    calenderdialog.setCanceledOnTouchOutside(false);
                    calenderdialog.show();
                    CalendarView calendarView;
                    calendarView = calenderdialog.findViewById(R.id.calendarView);
                    TextView cancel = calenderdialog.findViewById(R.id.cancel);
                    TextView done = calenderdialog.findViewById(R.id.done);
                    calendarView.setMaxDate(new Date().getTime());
                    strSelectedDate = null;
                    calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                        @Override
                        public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                            strSelectedDate = null;
                            int m = month + 1;
                            String currentmonth;
                            String currentday;
                            if (m < 10) {
                                currentmonth = "0" + m + "";
                            } else {
                                currentmonth = m + "";
                            }
                            if (dayOfMonth < 10) {
                                currentday = "0" + dayOfMonth;
                            } else {
                                currentday = dayOfMonth + "";
                            }
                            strSelectedDate = currentday + "-" + currentmonth + "-" + year;
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            calenderdialog.dismiss();
                        }
                    });

                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (strSelectedDate != null) {

                                mETMockDrillDate.setText(strSelectedDate);

                            } /*else {
                                mETDate.setText(part1);
                            }
                          Date selectedDate = null;
                            try {
                                selectedDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(mETDate.getText().toString() + " " + new SimpleDateFormat("HH:mm").format(new Date()) + ":00");
                            } catch (ParseException p) {
                                p.getMessage();
                            }

                            if (selectedDate.equals(new Date()) || selectedDate.before(new Date())) {
                                mETDate.setText(new SimpleDateFormat("HH:mm").format(selectedDate));
                            } else {
                                mETDate.setText(new SimpleDateFormat("HH:mm").format(new Date()));
                            }*/
                            calenderdialog.dismiss();
                        }
                    });
                }
            });

            mETDate.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    mETDate.setFocusableInTouchMode(false);
                    mETDate.setFocusable(false);
                    mETDate.setError(null);
                    final Dialog calenderdialog = new Dialog(CreateManpowerActivity.this, R.style.MyDialogTheme);
                    calenderdialog.setContentView(R.layout.activity_incidentdate);
                    calenderdialog.setCanceledOnTouchOutside(false);
                    calenderdialog.show();
                    CalendarView calendarView;
                    calendarView = calenderdialog.findViewById(R.id.calendarView);
                    TextView cancel = calenderdialog.findViewById(R.id.cancel);
                    TextView done = calenderdialog.findViewById(R.id.done);
                    calendarView.setMaxDate(new Date().getTime());
                    strSelectedDate = null;
                    calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                        @Override
                        public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                            strSelectedDate = null;
                            int m = month + 1;
                            String currentmonth;
                            String currentday;
                            if (m < 10) {
                                currentmonth = "0" + m + "";
                            } else {
                                currentmonth = m + "";
                            }
                            if (dayOfMonth < 10) {
                                currentday = "0" + dayOfMonth;
                            } else {
                                currentday = dayOfMonth + "";
                            }
                            strSelectedDate = currentday + "-" + currentmonth + "-" + year;
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            calenderdialog.dismiss();
                        }
                    });

                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (strSelectedDate != null) {

                                mETDate.setText(strSelectedDate);

                            } /*else {
                                mETDate.setText(part1);
                            }
                          Date selectedDate = null;
                            try {
                                selectedDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(mETDate.getText().toString() + " " + new SimpleDateFormat("HH:mm").format(new Date()) + ":00");
                            } catch (ParseException p) {
                                p.getMessage();
                            }

                            if (selectedDate.equals(new Date()) || selectedDate.before(new Date())) {
                                mETDate.setText(new SimpleDateFormat("HH:mm").format(selectedDate));
                            } else {
                                mETDate.setText(new SimpleDateFormat("HH:mm").format(new Date()));
                            }*/
                            calenderdialog.dismiss();
                        }
                    });
                }
            });

            mETFireExtinguishers.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getSafetyInspectionCount();
                }
            });

            mETELCB.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getSafetyInspectionCount();
                }
            });

            mETElectrical.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getSafetyInspectionCount();
                }
            });

            mETFullBodyHarness.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getSafetyInspectionCount();
                }
            });

            mETPPES.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getSafetyInspectionCount();
                }
            });

            mETLiftingEquipment.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getSafetyInspectionCount();
                }
            });

            mETGrindingMachine.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getSafetyInspectionCount();
                }
            });

            mETWeldingMachine.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getSafetyInspectionCount();
                }
            });

            mETGasCuttingMachine.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getSafetyInspectionCount();
                }
            });

            mETOfficeStore.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getSafetyInspectionCount();
                }
            });

            mETScaffoldLadder.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getSafetyInspectionCount();
                }
            });

            mETThirdPartyVehicle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getSafetyInspectionCount();
                }
            });

            mETFireDrillCount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    int intCount1 = 0, intCount2 = 0, intTotal;
                    try {

                        if (mETFireDrillCount.getText().toString().length() > 0) {

                            intCount1 = Integer.parseInt(mETFireDrillCount.getText().toString().trim());
                        }
                        if (mETMockDrillCount.getText().toString().length() > 0) {

                            intCount2 = Integer.parseInt(mETMockDrillCount.getText().toString().trim());
                        }
                        intTotal = intCount1 + intCount2;
                        mETEmergencyTotal.setText(intTotal + "");
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            });

            mETMockDrillCount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    int intCount1 = 0, intCount2 = 0, intTotal;
                    try {

                        if (mETFireDrillCount.getText().toString().length() > 0) {

                            intCount1 = Integer.parseInt(mETFireDrillCount.getText().toString().trim());
                        }
                        if (mETMockDrillCount.getText().toString().length() > 0) {

                            intCount2 = Integer.parseInt(mETMockDrillCount.getText().toString().trim());
                        }
                        intTotal = intCount1 + intCount2;
                        mETEmergencyTotal.setText(intTotal + "");
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            });

            mETElectricalFire.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getNearMissCount();
                }
            });
            mETSlipTrip.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getNearMissCount();
                }
            });
            mETFallOfMaterial.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getNearMissCount();
                }
            });
            mETEquipment.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getNearMissCount();
                }
            });
            mETCollision.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getNearMissCount();
                }
            });
            mETToppling.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getNearMissCount();
                }
            });
            mETFloorOpening.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getNearMissCount();
                }
            });
            mETVehicleMovement.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    getNearMissCount();
                }
            });
            mETNSD.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getSafetyEventsCount();
                }
            });
            mETRoad.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getSafetyEventsCount();
                }
            });
            mETWED.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getSafetyEventsCount();
                }
            });
            mETHealth.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getSafetyEventsCount();
                }
            });
            mETOther.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getSafetyEventsCount();
                }
            });
            mETSrLeadership.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getAuditsCount();
                }
            });
            mETInternal.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getAuditsCount();
                }
            });
            mETExternal.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getAuditsCount();
                }
            });
            mETSrManagement.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getSafetyCommunicationCount();
                }
            });
            mETBUHeads.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getSafetyCommunicationCount();
                }
            });
            mETSLLB.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getSafetyCommunicationCount();
                }
            });
            mETNoOfRewardGiven.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getSafetyCommunicationCount();
                }
            });
            mETNoOfSafetyViolationMemo.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    getSafetyCommunicationCount();
                }
            });
            mETStructureCollapse.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    getDangerousOccurenceCount();
                }
            });
            mETHighPressureRelease.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getDangerousOccurenceCount();
                }
            });
            mETHighLowTempExposure.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getDangerousOccurenceCount();
                }
            });
            mETBurstingMachine.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    getDangerousOccurenceCount();
                }
            });
            mETFatal.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    // getInjuriesCount();
                }
            });
            mETLTI.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    int count = 0, count1 = 0;
                    try {
                        if (mETLTI.getText().toString().length() > 0) {
                            count = Integer.parseInt(mETLTI.getText().toString().trim());
                        }
                        if (mETHeadCount.getText().toString().length() > 0) {
                            count1 = Integer.parseInt(mETHeadCount.getText().toString().trim()) * 8 * 26;
                        }

                        float total = (count * 1000000) / count1;
                        mETLTIFRPercent.setText(total + "");

                    } catch (Exception e) {
                        e.getMessage();
                    }
                    getInjuriesCount();
                }
            });
            mETMTI.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    int count = 0, count1 = 0;
                    try {
                        if (mETMTI.getText().toString().length() > 0) {
                            count = Integer.parseInt(mETMTI.getText().toString().trim());
                        }
                        if (mETHeadCount.getText().toString().length() > 0) {
                            count1 = Integer.parseInt(mETHeadCount.getText().toString().trim()) * 8 * 26;
                        }


                        float total = (count * 1000000) / count1;
                        mETMTIFRPercent.setText(total + "");

                    } catch (Exception e) {
                        e.getMessage();
                    }
                    getInjuriesCount();
                }
            });
            mETFirstAid.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    getInjuriesCount();
                }
            });
            mETLTIFR.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getInjurieRateCount();
                }
            });
            mETMTIFR.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getInjurieRateCount();
                }
            });
            mETAIFR.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getInjurieRateCount();
                }
            });
            mETInjuriesTotal.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    int count = 0, count1 = 0;
                    try {
                        if (mETLTI.getText().toString().length() > 0 && mETMTI.getText().toString().length() > 0 && mETFirstAid.getText().toString().length() > 0) {
                            count = Integer.parseInt(mETLTI.getText().toString().trim()) + Integer.parseInt(mETMTI.getText().toString().trim()) + Integer.parseInt(mETFirstAid.getText().toString().trim());
                        }

                        if (mETHeadCount.getText().toString().length() > 0) {
                            count1 = Integer.parseInt(mETHeadCount.getText().toString().trim()) * 8 * 26;
                        }

                        float total = (count * 1000000) / count1;
                        mETAIFRPercent.setText(total + "");

                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
            });

            mETPPE.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getObservationCount();
                }
            });

            mETWorkAtHeight.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getObservationCount();
                }
            });

            mETEnvironment.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getObservationCount();
                }
            });
            mETLowRisk.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getObservationCount();
                }
            });
            mETMediumRisk.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getObservationCount();
                }
            });
            mETHighRisk.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getObservationCount();
                }
            });
            mETCloseObservation.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getObservationCount();
                }
            });
            mETOpenObservation.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getObservationCount();
                }
            });
            /*mSPVertical.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (adapterView.getSelectedItem().toString().equalsIgnoreCase("Select Vertical *")) {

                    } else {


                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });*/

            mSPZone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (adapterView.getSelectedItem().toString().equalsIgnoreCase("Select Zone *")) {
                        zoneID = "";
                        mSPBranch.setAdapter(null);
                    } else {

                        zoneID = zonesArrayListId.get(i);
                        getBranches();

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            mSPBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (adapterView.getSelectedItem().toString().equalsIgnoreCase("Select Branch *")) {
                        branchID = "";
                    } else {
                        branchID = branchArrayListId.get(i);

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            mCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    finish();
                }
            });

            mCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            mNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        if (validate()) {

                            if (objManpowerInput.getId() != null) {

                            } else {
                                objManpowerInput.setProjectName(mETSiteProjectName.getText().toString().trim());
                                objManpowerInput.setVertical(mSPVertical.getSelectedItem().toString());
                                objManpowerInput.setZone(zoneID);
                                objManpowerInput.setBranch(branchID);


                                Date misDate = null;
                                try {
                                    misDate = mDateSDF.parse(mETDate.getText().toString().trim());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                //observationHeader.setTargetDateOfClosure(output.format(data) + "00:00:00");
                                objManpowerInput.setMisdate(mDateOutput.format(misDate) + "00:00:00");
                                objManpowerInput.setStaffHeadcount(Integer.parseInt(mETHeadCount.getText().toString().trim()));
                                objManpowerInput.setContractHeadcount(Integer.parseInt(mETHeadCountContract.getText().toString().trim()));
                                objManpowerInput.setStaffWorkedHours(Integer.parseInt(mETManHoursWorked.getText().toString().trim()));
                                objManpowerInput.setContractWorkedHours(Integer.parseInt(mETManHoursWorkedContract.getText().toString().trim()));

                                objManpowerInput.setSoeppe(Integer.parseInt(mETPPE.getText().toString().trim()));
                                objManpowerInput.setSoeworkatHeight(Integer.parseInt(mETWorkAtHeight.getText().toString().trim()));
                                objManpowerInput.setSoeenvironment(Integer.parseInt(mETEnvironment.getText().toString().trim()));
                                objManpowerInput.setSoelowRisk(Integer.parseInt(mETLowRisk.getText().toString().trim()));
                                objManpowerInput.setSoemediumRisk(Integer.parseInt(mETMediumRisk.getText().toString().trim()));
                                objManpowerInput.setSoehighRisk(Integer.parseInt(mETHighRisk.getText().toString().trim()));
                                objManpowerInput.setSoecloseObservation(Integer.parseInt(mETCloseObservation.getText().toString().trim()));
                                objManpowerInput.setSoeopenObservation(Integer.parseInt(mETOpenObservation.getText().toString().trim()));

                                objManpowerInput.setInternalOtherName(mETInternalOtherName.getText().toString().trim());
                                objManpowerInput.setExternalOtherName(mETExternalOtherName.getText().toString().trim());

                                objManpowerInput.setManagerName(mETProjectManagerName.getText().toString().trim());
                                objManpowerInput.setManagerEmail(mETProjectManagerMail.getText().toString().trim());
                                objManpowerInput.setManagerContact(mETProjectManagerMobileno.getText().toString().trim());
                                objManpowerInput.setSafetyOfficerName(mETSafetyOfficerName.getText().toString().trim());
                                objManpowerInput.setSafetyOfficerEmail(mETSafetyOfficerMail.getText().toString().trim());
                                objManpowerInput.setSafetyOfficerContact(mETSafetyOfficerMobileno.getText().toString().trim());
                                objManpowerInput.setPdorBuname(mETPDBUName.getText().toString().trim());
                                objManpowerInput.setPdorBuemail(mETPDBUMail.getText().toString().trim());
                                objManpowerInput.setPdorBucontact(mETPDBUMobileno.getText().toString().trim());

                                objManpowerInput.setNoOfParticipantsInToolBoxTalk(Integer.parseInt(mETNoOfParticipents.getText().toString().trim()));
                                objManpowerInput.setNoOfSafetyMeetings(Integer.parseInt(mETNoOFSafetyMeetings.getText().toString().trim()));
                                objManpowerInput.setNoOfSafetyObservationsUnsafeAct(Integer.parseInt(mETNoOfUnsafeActs.getText().toString().trim()));
                                objManpowerInput.setNoOfSafetyObservationsUnsafeCondition(Integer.parseInt(mETNoOfUnsafeConditions.getText().toString().trim()));
                                //objManpowerInput.setNoOfShemeetings(Integer.parseInt(mETNoOfSheMeetings.getText().toString().trim()));
                                objManpowerInput.setNoOfSafetyInspectionsFireExtinguishers(Integer.parseInt(mETFireExtinguishers.getText().toString().trim()));
                                objManpowerInput.setNoOfSafetyInspectionsElcb(Integer.parseInt(mETELCB.getText().toString().trim()));
                                objManpowerInput.setNoOfSafetyInspectionsElectrical(Integer.parseInt(mETElectrical.getText().toString().trim()));
                                objManpowerInput.setNoOfSafetyInspectionsFullBodyHarness(Integer.parseInt(mETFullBodyHarness.getText().toString().trim()));
                                objManpowerInput.setNoOfSafetyInspectionsPppes(Integer.parseInt(mETPPES.getText().toString().trim()));
                                objManpowerInput.setNoOfSafetyInspectionsLiftingEquipment(Integer.parseInt(mETLiftingEquipment.getText().toString().trim()));
                                objManpowerInput.setNoOfSafetyInspectionsGrindingMachine(Integer.parseInt(mETGrindingMachine.getText().toString().trim()));
                                objManpowerInput.setNoOfSafetyInspectionsWeldingMachine(Integer.parseInt(mETWeldingMachine.getText().toString().trim()));
                                objManpowerInput.setNoOfSafetyInspectionsGasCuttingMachine(Integer.parseInt(mETGasCuttingMachine.getText().toString().trim()));
                                objManpowerInput.setNoOfSafetyInspectionsOfficeStore(Integer.parseInt(mETOfficeStore.getText().toString().trim()));
                                objManpowerInput.setNoOfSafetyInspectionsScaffoldLadder(Integer.parseInt(mETScaffoldLadder.getText().toString().trim()));
                                objManpowerInput.setNoOfSafetyInspectionsThirdPartyVehicle(Integer.parseInt(mETThirdPartyVehicle.getText().toString().trim()));
                                //objManpowerInput.sett(Integer.parseInt(mETFullBodyHarness.getText().toString().trim()));
                                Date fireDate = null, mockDate = null;
                                try {
                                    fireDate = mDateSDF.parse(mETFireDrillDate.getText().toString().trim());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    mockDate = mDateSDF.parse(mETMockDrillDate.getText().toString().trim());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                //observationHeader.setTargetDateOfClosure(output.format(data) + "00:00:00");
                                objManpowerInput.setEmergencyResponseFireDrillCount(Integer.parseInt(mETFireDrillCount.getText().toString().trim()));
                                objManpowerInput.setEmergencyResponseFireDrillDate(mDateOutput.format(fireDate) + "00:00:00");
                                objManpowerInput.setEmergencyResponseMockDrillCount(Integer.parseInt(mETMockDrillCount.getText().toString().trim()));
                                objManpowerInput.setEmergencyResponseMockDrillDate(mDateOutput.format(mockDate) + "00:00:00");

                                objManpowerInput.setNearMissElectricalFire(Integer.parseInt(mETElectricalFire.getText().toString().trim()));
                                objManpowerInput.setNearMissSlipTrip(Integer.parseInt(mETSlipTrip.getText().toString().trim()));
                                objManpowerInput.setNearMissFallOfMaterial(Integer.parseInt(mETFallOfMaterial.getText().toString().trim()));
                                objManpowerInput.setNearMissEquipment(Integer.parseInt(mETEquipment.getText().toString().trim()));
                                objManpowerInput.setNearMissCollision(Integer.parseInt(mETCollision.getText().toString().trim()));
                                objManpowerInput.setNearMissToppling(Integer.parseInt(mETToppling.getText().toString().trim()));
                                objManpowerInput.setNearMissFloorOpening(Integer.parseInt(mETFloorOpening.getText().toString().trim()));
                                objManpowerInput.setNearMissVehicleMovement(Integer.parseInt(mETVehicleMovement.getText().toString().trim()));

                                objManpowerInput.setSafetyEventsNsd(Integer.parseInt(mETNSD.getText().toString().trim()));
                                objManpowerInput.setSafetyEventsRoad(Integer.parseInt(mETRoad.getText().toString().trim()));
                                objManpowerInput.setSafetyEventsWed(Integer.parseInt(mETWED.getText().toString().trim()));
                                objManpowerInput.setSafetyEventsHealth(Integer.parseInt(mETHealth.getText().toString().trim()));
                                objManpowerInput.setSafetyEventsOther(Integer.parseInt(mETOther.getText().toString().trim()));

                                objManpowerInput.setNoOfAuditsSrLeadership(Integer.parseInt(mETSrLeadership.getText().toString().trim()));
                                objManpowerInput.setNoOfAuditsInternal(Integer.parseInt(mETInternal.getText().toString().trim()));
                                objManpowerInput.setNoOfAuditsExternal(Integer.parseInt(mETExternal.getText().toString().trim()));

                                objManpowerInput.setSafetyCommunicationsSrManagement(Integer.parseInt(mETSrManagement.getText().toString().trim()));
                                objManpowerInput.setSafetyCommunicationsBuheads(Integer.parseInt(mETBUHeads.getText().toString().trim()));
                                objManpowerInput.setSafetyCommunicationsSllb(Integer.parseInt(mETSLLB.getText().toString().trim()));
                                objManpowerInput.setNoOfRewardGiven(Integer.parseInt(mETNoOfRewardGiven.getText().toString().trim()));
                                objManpowerInput.setNoOfSafetViolationMemoIssued(Integer.parseInt(mETNoOfSafetyViolationMemo.getText().toString().trim()));
                                //objManpowerInput.setco(Integer.parseInt(mETSrLeadership.getText().toString().trim()));

                                objManpowerInput.setDangerousOccurrencesStructureCollapse(Integer.parseInt(mETStructureCollapse.getText().toString().trim()));
                                objManpowerInput.setDangerousOccurrencesHighPreassureRelease(Integer.parseInt(mETHighPressureRelease.getText().toString().trim()));
                                objManpowerInput.setDangerousOccurencesHighLowTempExposure(Integer.parseInt(mETHighLowTempExposure.getText().toString().trim()));
                                objManpowerInput.setDangerousOccurencesBurstiingOfMachine(Integer.parseInt(mETBurstingMachine.getText().toString().trim()));
                                //objManpowerInput.setNoOfSafetViolationMemoIssued(Integer.parseInt(mETNoOfSafetyViolationMemo.getText().toString().trim()));
                                //objManpowerInput.setco(Integer.parseInt(mETSrLeadership.getText().toString().trim()));

                                objManpowerInput.setNoOfInjuriesFatal(Integer.parseInt(mETFatal.getText().toString().trim()));
                                objManpowerInput.setNoOfInjuriesLti(Integer.parseInt(mETLTI.getText().toString().trim()));
                                objManpowerInput.setNoOfInjuriesMti(Integer.parseInt(mETMTI.getText().toString().trim()));
                                objManpowerInput.setNoOfInjuriesFirstAid(Integer.parseInt(mETFirstAid.getText().toString().trim()));
                                //objManpowerInput.setNoOfSafetViolationMemoIssued(Integer.parseInt(mETNoOfSafetyViolationMemo.getText().toString().trim()));

                                objManpowerInput.setInjuryRateLtifr(Integer.parseInt(mETLTIFR.getText().toString().trim()));
                                objManpowerInput.setInjuryRateLtifrPer(Float.parseFloat(mETLTIFRPercent.getText().toString().trim()));
                                objManpowerInput.setInjuryRateMtifr(Integer.parseInt(mETMTIFR.getText().toString().trim()));
                                objManpowerInput.setInjuryRateMtifrPer(Float.parseFloat(mETMTIFRPercent.getText().toString().trim()));
                                objManpowerInput.setInjuryRateAfr(Integer.parseInt(mETAIFR.getText().toString().trim()));
                                objManpowerInput.setInjuryRateAfrPer(Float.parseFloat(mETAIFRPercent.getText().toString().trim()));
                            }
                            Intent intent = new Intent(CreateManpowerActivity.this, CreateManpowerListActivity.class);
                            Bundle bd = new Bundle();
                            bd.putSerializable("HeaderObject", (Serializable) objManpowerInput);
                            intent.putExtras(bd);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void getInjurieRateCount() {
        try {
            int count = 0, count1 = 0, count2 = 0;
            if (mETLTIFR.getText().toString().trim().length() > 0) {
                count = Integer.parseInt(mETLTIFR.getText().toString().trim());
            }
            if (mETMTIFR.getText().toString().trim().length() > 0) {
                count1 = Integer.parseInt(mETMTIFR.getText().toString().trim());
            }
            if (mETAIFR.getText().toString().trim().length() > 0) {
                count2 = Integer.parseInt(mETAIFR.getText().toString().trim());
            }

            int intTotal = count + count1 + count2;
            mETInjuriesRateTotal.setText(intTotal + "");
        } catch (Exception e) {

            e.getMessage();
        }
    }

    private void getInjuriesCount() {
        try {
            int count = 0, count1 = 0, count2 = 0, count3 = 0;
            if (mETFatal.getText().toString().trim().length() > 0) {
                count = Integer.parseInt(mETFatal.getText().toString().trim());
            }
            if (mETLTI.getText().toString().trim().length() > 0) {
                count1 = Integer.parseInt(mETLTI.getText().toString().trim());
            }
            if (mETMTI.getText().toString().trim().length() > 0) {
                count2 = Integer.parseInt(mETMTI.getText().toString().trim());
            }
            if (mETFirstAid.getText().toString().trim().length() > 0) {
                count3 = Integer.parseInt(mETFirstAid.getText().toString().trim());
            }

            intTotal = count + count1 + count2 + count3;
            mETInjuriesTotal.setText(intTotal + "");
        } catch (Exception e) {

            e.getMessage();
        }
    }

    private void getDangerousOccurenceCount() {
        try {
            int count = 0, count1 = 0, count2 = 0, count3 = 0;
            if (mETStructureCollapse.getText().toString().trim().length() > 0) {
                count = Integer.parseInt(mETStructureCollapse.getText().toString().trim());
            }
            if (mETHighPressureRelease.getText().toString().trim().length() > 0) {
                count1 = Integer.parseInt(mETHighPressureRelease.getText().toString().trim());
            }
            if (mETHighLowTempExposure.getText().toString().trim().length() > 0) {
                count2 = Integer.parseInt(mETHighLowTempExposure.getText().toString().trim());
            }
            if (mETBurstingMachine.getText().toString().trim().length() > 0) {
                count3 = Integer.parseInt(mETBurstingMachine.getText().toString().trim());
            }

            intTotal = count + count1 + count2 + count3;
            mETDangerousTotal.setText(intTotal + "");
        } catch (Exception e) {

            e.getMessage();
        }
    }

    private void getSafetyCommunicationCount() {
        try {
            int count = 0, count1 = 0, count2 = 0, count3 = 0, count4 = 0;
            if (mETSrManagement.getText().toString().trim().length() > 0) {
                count = Integer.parseInt(mETSrManagement.getText().toString().trim());
            }
            if (mETBUHeads.getText().toString().trim().length() > 0) {
                count1 = Integer.parseInt(mETBUHeads.getText().toString().trim());
            }
            if (mETSLLB.getText().toString().trim().length() > 0) {
                count2 = Integer.parseInt(mETSLLB.getText().toString().trim());
            }
            if (mETNoOfRewardGiven.getText().toString().trim().length() > 0) {
                count3 = Integer.parseInt(mETNoOfRewardGiven.getText().toString().trim());
            }
            if (mETNoOfSafetyViolationMemo.getText().toString().trim().length() > 0) {
                count4 = Integer.parseInt(mETNoOfSafetyViolationMemo.getText().toString().trim());
            }

            intTotal = count + count1 + count2 + count3 + count4;
            mETSafetyCommTotal.setText(intTotal + "");
        } catch (Exception e) {

            e.getMessage();
        }
    }

    private void getAuditsCount() {
        try {
            int count = 0, count1 = 0, count2 = 0;
            if (mETSrLeadership.getText().toString().trim().length() > 0) {
                count = Integer.parseInt(mETSrLeadership.getText().toString().trim());
            }
            if (mETInternal.getText().toString().trim().length() > 0) {
                count1 = Integer.parseInt(mETInternal.getText().toString().trim());
            }
            if (mETExternal.getText().toString().trim().length() > 0) {
                count2 = Integer.parseInt(mETExternal.getText().toString().trim());
            }
            intTotal = count + count1 + count2;
            mETAuditTotal.setText(intTotal + "");
        } catch (Exception e) {

            e.getMessage();
        }
    }

    private void getSafetyEventsCount() {
        try {
            int count = 0, count1 = 0, count2 = 0, count3 = 0, count4 = 0;
            if (mETNSD.getText().toString().trim().length() > 0) {
                count = Integer.parseInt(mETNSD.getText().toString().trim());
            }
            if (mETRoad.getText().toString().trim().length() > 0) {
                count1 = Integer.parseInt(mETRoad.getText().toString().trim());
            }
            if (mETWED.getText().toString().trim().length() > 0) {
                count2 = Integer.parseInt(mETWED.getText().toString().trim());
            }
            if (mETHealth.getText().toString().trim().length() > 0) {
                count3 = Integer.parseInt(mETHealth.getText().toString().trim());
            }
            if (mETOther.getText().toString().trim().length() > 0) {
                count4 = Integer.parseInt(mETOther.getText().toString().trim());
            }

            intTotal = count + count1 + count2 + count3 + count4;
            mETSafetyEventsTotal.setText(intTotal + "");
        } catch (Exception e) {

            e.getMessage();
        }
    }

    private void getObservationCount() {
        try {
            int count = 0, count1 = 0, count2 = 0, count3 = 0, count4 = 0, count5 = 0, count6 = 0, count7 = 0;
            if (mETPPE.getText().toString().trim().length() > 0) {
                count = Integer.parseInt(mETPPE.getText().toString().trim());
            }
            if (mETWorkAtHeight.getText().toString().trim().length() > 0) {
                count1 = Integer.parseInt(mETWorkAtHeight.getText().toString().trim());
            }
            if (mETEnvironment.getText().toString().trim().length() > 0) {
                count2 = Integer.parseInt(mETEnvironment.getText().toString().trim());
            }
            if (mETLowRisk.getText().toString().trim().length() > 0) {
                count3 = Integer.parseInt(mETLowRisk.getText().toString().trim());
            }
            if (mETMediumRisk.getText().toString().trim().length() > 0) {
                count4 = Integer.parseInt(mETMediumRisk.getText().toString().trim());
            }
            if (mETHighRisk.getText().toString().trim().length() > 0) {
                count5 = Integer.parseInt(mETHighRisk.getText().toString().trim());
            }
            if (mETCloseObservation.getText().toString().trim().length() > 0) {
                count6 = Integer.parseInt(mETCloseObservation.getText().toString().trim());
            }
            if (mETOpenObservation.getText().toString().trim().length() > 0) {
                count7 = Integer.parseInt(mETOpenObservation.getText().toString().trim());
            }
            intTotal = count + count1 + count2 + count3 + count4 + count5 + count6 + count7;
            mETObservationTotal.setText(intTotal + "");
        } catch (Exception e) {

            e.getMessage();
        }
    }

    private void getNearMissCount() {
        try {
            int count = 0, count1 = 0, count2 = 0, count3 = 0, count4 = 0, count5 = 0, count6 = 0, count7 = 0;
            if (mETElectricalFire.getText().toString().trim().length() > 0) {
                count = Integer.parseInt(mETElectricalFire.getText().toString().trim());
            }
            if (mETSlipTrip.getText().toString().trim().length() > 0) {
                count1 = Integer.parseInt(mETSlipTrip.getText().toString().trim());
            }
            if (mETFallOfMaterial.getText().toString().trim().length() > 0) {
                count2 = Integer.parseInt(mETFallOfMaterial.getText().toString().trim());
            }
            if (mETEquipment.getText().toString().trim().length() > 0) {
                count3 = Integer.parseInt(mETEquipment.getText().toString().trim());
            }
            if (mETCollision.getText().toString().trim().length() > 0) {
                count4 = Integer.parseInt(mETCollision.getText().toString().trim());
            }
            if (mETToppling.getText().toString().trim().length() > 0) {
                count5 = Integer.parseInt(mETToppling.getText().toString().trim());
            }
            if (mETFloorOpening.getText().toString().trim().length() > 0) {
                count6 = Integer.parseInt(mETFloorOpening.getText().toString().trim());
            }
            if (mETVehicleMovement.getText().toString().trim().length() > 0) {
                count7 = Integer.parseInt(mETVehicleMovement.getText().toString().trim());
            }
            intTotal = count + count1 + count2 + count3 + count4 + count5 + count6 + count7;
            mETNearmissTotal.setText(intTotal + "");
        } catch (Exception e) {

            e.getMessage();
        }
    }

    private void getSafetyInspectionCount() {
        try {
            int count = 0, count1 = 0, count2 = 0, count3 = 0, count4 = 0, count5 = 0, count6 = 0, count7 = 0, count8 = 0, count9 = 0,
                    count10 = 0, count11 = 0;
            if (mETFireExtinguishers.getText().toString().trim().length() > 0) {
                count = Integer.parseInt(mETFireExtinguishers.getText().toString().trim());
            }
            if (mETELCB.getText().toString().trim().length() > 0) {
                count1 = Integer.parseInt(mETELCB.getText().toString().trim());
            }
            if (mETElectrical.getText().toString().trim().length() > 0) {
                count2 = Integer.parseInt(mETElectrical.getText().toString().trim());
            }
            if (mETFullBodyHarness.getText().toString().trim().length() > 0) {
                count3 = Integer.parseInt(mETFullBodyHarness.getText().toString().trim());
            }
            if (mETPPES.getText().toString().trim().length() > 0) {
                count4 = Integer.parseInt(mETPPES.getText().toString().trim());
            }
            if (mETLiftingEquipment.getText().toString().trim().length() > 0) {
                count5 = Integer.parseInt(mETLiftingEquipment.getText().toString().trim());
            }
            if (mETGrindingMachine.getText().toString().trim().length() > 0) {
                count6 = Integer.parseInt(mETGrindingMachine.getText().toString().trim());
            }
            if (mETWeldingMachine.getText().toString().trim().length() > 0) {
                count7 = Integer.parseInt(mETWeldingMachine.getText().toString().trim());
            }
            if (mETGasCuttingMachine.getText().toString().trim().length() > 0) {
                count8 = Integer.parseInt(mETGasCuttingMachine.getText().toString().trim());
            }
            if (mETOfficeStore.getText().toString().trim().length() > 0) {
                count9 = Integer.parseInt(mETOfficeStore.getText().toString().trim());
            }
            if (mETScaffoldLadder.getText().toString().trim().length() > 0) {
                count10 = Integer.parseInt(mETScaffoldLadder.getText().toString().trim());
            }
            if (mETThirdPartyVehicle.getText().toString().trim().length() > 0) {
                count11 = Integer.parseInt(mETThirdPartyVehicle.getText().toString().trim());
            }
            intTotal = count + count1 + count2 + count3 + count4 + count5 + count6 + count7 + count8 + count9 + count10 + count11;
            mETTotal.setText(intTotal + "");
        } catch (Exception e) {

            e.getMessage();
        }
    }

    private void getVerticle() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<Verticleresponse> call = apiInterface.getVerticleData("Bearer " + sharedPreferences.getString("Bearertoken", null));
            showProgressDialog(CreateManpowerActivity.this);
            call.enqueue(new Callback<Verticleresponse>() {
                public void onResponse(Call<Verticleresponse> call, Response<Verticleresponse> response) {
                    verticleArrayList = new ArrayList<>();
                    verticleArrayList.add("Select Vertical *");
                    if (response.isSuccessful()) {
                        dismissProgress();
                        if (response.body().getResult() != null) {

                            for (int i = 0; i < response.body().getResult().size(); i++) {
                                verticleArrayList.add(response.body().getResult().get(i).getClientName());
                            }
                            verticalAdapter = new ArrayAdapter(CreateManpowerActivity.this, android.R.layout.simple_list_item_1, verticleArrayList);
                            verticalAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                            mSPVertical.setAdapter(verticalAdapter);
                            if (objManpowerInput.getVertical() != null) {
                                String branch = objManpowerInput.getVertical();
                                int position = verticalAdapter.getPosition(branch);
                                mSPVertical.setSelection(position);
                            }
                            getZones();

                        }
                    } else {
                        dismissProgress();
                        showAlert(response.message() + "");
                    }
                }


                public void onFailure(Call<Verticleresponse> call, Throwable t) {
                    dismissProgress();
                    Log.d("LoginResponse", t.getMessage() + "");
                }


            });
        } catch (Exception e) {
            e.getMessage();

        }
    }

    private void getZones() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<UserZoneResponse> call = apiInterface.getGuestZoneResponse("Bearer " + sharedPreferences.getString("Bearertoken", null), sharedPreferences.getString("Domain", null));
            showProgressDialog(CreateManpowerActivity.this);
            call.enqueue(new Callback<UserZoneResponse>() {
                public void onResponse(Call<UserZoneResponse> call, Response<UserZoneResponse> response) {
                    zonesArrayList = new ArrayList<>();
                    zonesArrayListId = new ArrayList<>();
                    zonesArrayListId.add("");
                    zonesArrayList.add("Select Zone *");
                    if (response.isSuccessful()) {
                        dismissProgress();

                        for (int i = 0; i < response.body().getResult().size(); i++) {
                            zonesArrayList.add(response.body().getResult().get(i).getZoneName());
                            zonesArrayListId.add(response.body().getResult().get(i).getZoneId());

                        }

                        zoneAdapter = new ArrayAdapter(CreateManpowerActivity.this, android.R.layout.simple_list_item_1, zonesArrayList);
                        zoneAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                        mSPZone.setAdapter(zoneAdapter);
                        int position = 0;
                        if (objManpowerInput.getZone() != null) {
                            String branch = objManpowerInput.getZone();
                            for (int i = 0; i < zonesArrayListId.size(); i++) {

                                if (branch.equalsIgnoreCase(zonesArrayListId.get(i))) {
                                    position = i;
                                }
                            }
                            //= zonesArrayListId.getPosition(branch);
                            mSPZone.setSelection(position);
                        }
                    }
                }


                public void onFailure(Call<UserZoneResponse> call, Throwable t) {
                    dismissProgress();
                    Log.d("LoginResponse", t.getMessage() + "");
                }


            });
        } catch (Exception e) {
            e.getMessage();
            dismissProgress();

        }
    }

    private void getBranches() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<UserBranchResponse> call = apiInterface.getGuestBranchResponse("Bearer " + sharedPreferences.getString("Bearertoken", null), zoneID);
            showProgressDialog(CreateManpowerActivity.this);
            call.enqueue(new Callback<UserBranchResponse>() {
                public void onResponse(Call<UserBranchResponse> call, Response<UserBranchResponse> response) {
                    branchArrayList = new ArrayList<>();
                    branchArrayListId = new ArrayList<>();
                    branchArrayListId.add("");
                    branchArrayList.add("Select Branch *");
                    if (response.isSuccessful()) {
                        dismissProgress();
                        Log.d("", response.toString());
                        for (int i = 0; i < response.body().getResult().size(); i++) {
                            branchArrayList.add(response.body().getResult().get(i).getBranchName());
                            branchArrayListId.add(response.body().getResult().get(i).getBranchId());

                        }
                        branchArrayAdapter = new ArrayAdapter(CreateManpowerActivity.this, android.R.layout.simple_list_item_1, branchArrayList);
                        branchArrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                        mSPBranch.setAdapter(branchArrayAdapter);
                        int position = 0;
                        if (objManpowerInput.getBranch() != null) {
                            String branch = objManpowerInput.getBranch();
                            for (int i = 0; i < branchArrayListId.size(); i++) {

                                if (branch.equalsIgnoreCase(branchArrayListId.get(i))) {
                                    position = i;
                                }
                            }
                            // int position = branchArrayAdapter.getPosition(branch);
                            mSPBranch.setSelection(position);
                        }

                        //  getManPowerDetails();

                    } else {
                        dismissProgress();
                    }

                }


                public void onFailure(Call<UserBranchResponse> call, Throwable t) {
                    dismissProgress();
                    Log.d("LoginResponse", t.getMessage() + "");
                }


            });
        } catch (Exception e) {
            e.getMessage();
            dismissProgress();

        }
    }

    private void getManPowerDetails() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
            ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
            Call<ManPowerResponse> call = apiInterface.getManPower("Bearer " + sharedPreferences.getString("Bearertoken", null));
            showProgressDialog(CreateManpowerActivity.this);
            call.enqueue(new Callback<ManPowerResponse>() {
                public void onResponse(Call<ManPowerResponse> call, Response<ManPowerResponse> response) {

                    if (response.isSuccessful()) {
                        dismissProgress();

                        try {
                            if (response.body().getSuccess()) {
                                objManpowerInput = response.body().getResult();

                                mETSiteProjectName.setText(objManpowerInput.getProjectName());
                                //mSPVertical.setSelection(verticalAdapter.getPosition(objManpowerInput.getVertical()));
                                //mSPZone.setSelection(zoneAdapter.getPosition(objManpowerInput.getZone()));
                                //mSPBranch.setSelection(branchArrayAdapter.getPosition(objManpowerInput.getBranch()));
                                Date misDate = null;
                                try {
                                    misDate = mDateOutput.parse(objManpowerInput.getMisdate());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                mETDate.setText(mDateSDF.format(misDate));
                                mETHeadCount.setText("" + objManpowerInput.getStaffHeadcount());
                                mETHeadCountContract.setText("" + objManpowerInput.getContractHeadcount());
                                mETManHoursWorked.setText("" + objManpowerInput.getStaffWorkedHours());
                                mETManHoursWorkedContract.setText("" + objManpowerInput.getContractWorkedHours());

                                mETPPE.setText("" + objManpowerInput.getSoeppe());
                                mETWorkAtHeight.setText("" + objManpowerInput.getSoeworkatHeight());
                                mETEnvironment.setText("" + objManpowerInput.getSoeenvironment());
                                mETLowRisk.setText("" + objManpowerInput.getSoelowRisk());
                                mETMediumRisk.setText("" + objManpowerInput.getSoemediumRisk());
                                mETHighRisk.setText("" + objManpowerInput.getSoehighRisk());
                                mETCloseObservation.setText("" + objManpowerInput.getSoecloseObservation());
                                mETOpenObservation.setText("" + objManpowerInput.getSoeopenObservation());

                                mETInternalOtherName.setText("" + objManpowerInput.getInternalOtherName());
                                mETExternalOtherName.setText("" + objManpowerInput.getExternalOtherName());

                                mETProjectManagerName.setText(objManpowerInput.getManagerName());
                                mETProjectManagerMail.setText(objManpowerInput.getManagerEmail());
                                mETProjectManagerMobileno.setText(objManpowerInput.getManagerContact());

                                mETSafetyOfficerName.setText(objManpowerInput.getSafetyOfficerName());
                                mETSafetyOfficerMail.setText(objManpowerInput.getSafetyOfficerEmail());
                                mETSafetyOfficerMobileno.setText(objManpowerInput.getSafetyOfficerContact());

                                mETPDBUName.setText(objManpowerInput.getManagerName());
                                mETPDBUMail.setText(objManpowerInput.getManagerEmail());
                                mETPDBUMobileno.setText(objManpowerInput.getManagerContact());

                                mETNoOfParticipents.setText("" + objManpowerInput.getNoOfParticipantsInToolBoxTalk());
                                mETNoOFSafetyMeetings.setText("" + objManpowerInput.getNoOfSafetyMeetings());
                                mETNoOfUnsafeActs.setText("" + objManpowerInput.getNoOfSafetyObservationsUnsafeAct());
                                mETNoOfUnsafeConditions.setText("" + objManpowerInput.getNoOfSafetyObservationsUnsafeCondition());
                                //mETNoOfSheMeetings.setText(""+objManpowerInput.getNoOfShemeetings());

                                mETFireExtinguishers.setText("" + objManpowerInput.getNoOfSafetyInspectionsFireExtinguishers());
                                mETELCB.setText("" + objManpowerInput.getNoOfSafetyInspectionsElcb());
                                mETElectrical.setText("" + objManpowerInput.getNoOfSafetyInspectionsElectrical());
                                mETFullBodyHarness.setText("" + objManpowerInput.getNoOfSafetyInspectionsFullBodyHarness());
                                mETPPES.setText("" + objManpowerInput.getNoOfSafetyInspectionsPppes());
                                mETLiftingEquipment.setText("" + objManpowerInput.getNoOfSafetyInspectionsLiftingEquipment());
                                mETGrindingMachine.setText("" + objManpowerInput.getNoOfSafetyInspectionsGrindingMachine());
                                mETWeldingMachine.setText("" + objManpowerInput.getNoOfSafetyInspectionsWeldingMachine());
                                mETGasCuttingMachine.setText("" + objManpowerInput.getNoOfSafetyInspectionsGasCuttingMachine());
                                mETOfficeStore.setText("" + objManpowerInput.getNoOfSafetyInspectionsOfficeStore());
                                mETScaffoldLadder.setText("" + objManpowerInput.getNoOfSafetyInspectionsScaffoldLadder());
                                mETThirdPartyVehicle.setText("" + objManpowerInput.getNoOfSafetyInspectionsThirdPartyVehicle());
                                //mETTotal.setText(objManpowerInput.get());
                                mETFireDrillCount.setText("" + objManpowerInput.getEmergencyResponseFireDrillCount());
                                Date fireDate = null;
                                try {
                                    fireDate = mDateOutput.parse(objManpowerInput.getEmergencyResponseFireDrillDate());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                mETFireDrillDate.setText(mDateSDF.format(fireDate));
                                // mETFireDrillDate.setText(objManpowerInput.getEmergencyResponseFireDrillDate());
                                mETMockDrillCount.setText("" + objManpowerInput.getEmergencyResponseMockDrillCount());
                                Date mockDate = null;
                                try {
                                    mockDate = mDateOutput.parse(objManpowerInput.getEmergencyResponseMockDrillDate());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                mETMockDrillDate.setText(mDateSDF.format(mockDate));
                                // mETMockDrillDate.setText(objManpowerInput.getEmergencyResponseMockDrillDate());

                                mETElectricalFire.setText("" + objManpowerInput.getNearMissElectricalFire());
                                mETSlipTrip.setText("" + objManpowerInput.getNearMissSlipTrip());
                                mETFallOfMaterial.setText("" + objManpowerInput.getNearMissFallOfMaterial());
                                mETEquipment.setText("" + objManpowerInput.getNearMissEquipment());
                                mETCollision.setText("" + objManpowerInput.getNearMissCollision());
                                mETToppling.setText("" + objManpowerInput.getNearMissToppling());
                                mETFloorOpening.setText("" + objManpowerInput.getNearMissFloorOpening());
                                mETVehicleMovement.setText("" + objManpowerInput.getNearMissVehicleMovement());

                                mETNSD.setText("" + objManpowerInput.getSafetyEventsNsd());
                                mETRoad.setText("" + objManpowerInput.getSafetyEventsRoad());
                                mETWED.setText("" + objManpowerInput.getSafetyEventsWed());
                                mETHealth.setText("" + objManpowerInput.getSafetyEventsHealth());
                                mETOther.setText("" + objManpowerInput.getSafetyEventsOther());

                                mETSrLeadership.setText("" + objManpowerInput.getNoOfAuditsSrLeadership());
                                mETInternal.setText("" + objManpowerInput.getNoOfAuditsInternal());
                                mETExternal.setText("" + objManpowerInput.getNoOfAuditsExternal());

                                mETSrManagement.setText("" + objManpowerInput.getSafetyCommunicationsSrManagement());
                                mETBUHeads.setText("" + objManpowerInput.getSafetyCommunicationsBuheads());
                                mETSLLB.setText("" + objManpowerInput.getSafetyCommunicationsSllb());
                                mETNoOfRewardGiven.setText("" + objManpowerInput.getNoOfRewardGiven());
                                mETNoOfSafetyViolationMemo.setText("" + objManpowerInput.getNoOfSafetViolationMemoIssued());

                                mETStructureCollapse.setText("" + objManpowerInput.getDangerousOccurrencesStructureCollapse());
                                mETHighPressureRelease.setText("" + objManpowerInput.getDangerousOccurrencesHighPreassureRelease());
                                mETHighLowTempExposure.setText("" + objManpowerInput.getDangerousOccurencesHighLowTempExposure());
                                mETBurstingMachine.setText("" + objManpowerInput.getDangerousOccurencesBurstiingOfMachine());

                                mETFatal.setText("" + objManpowerInput.getNoOfInjuriesFatal());
                                mETLTI.setText("" + objManpowerInput.getNoOfInjuriesLti());
                                mETMTI.setText("" + objManpowerInput.getNoOfInjuriesMti());
                                mETFirstAid.setText("" + objManpowerInput.getNoOfInjuriesFirstAid());

                                mETLTIFR.setText("" + objManpowerInput.getInjuryRateLtifr());
                                mETMTIFR.setText("" + objManpowerInput.getInjuryRateMtifr());
                                mETLTIFRPercent.setText("" + objManpowerInput.getInjuryRateLtifrPer());
                                mETMTIFRPercent.setText("" + objManpowerInput.getInjuryRateMtifrPer());
                                mETAIFR.setText("" + objManpowerInput.getInjuryRateAfr());
                                mETAIFRPercent.setText("" + objManpowerInput.getInjuryRateAfrPer());

                                Log.d("", response.toString());
                            } else {

                            }
                        } catch (Exception e) {
                            e.getMessage();
                        }


                    } else {
                        dismissProgress();
                    }
                    getVerticle();
                }

                public void onFailure(Call<ManPowerResponse> call, Throwable t) {
                    dismissProgress();
                    Log.d("LoginResponse", t.getMessage() + "");
                }


            });
        } catch (Exception e) {
            e.getMessage();
            dismissProgress();

        }
    }

    private boolean validate() {
        try {

            if (mETSiteProjectName.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Project Name";
                mETSiteProjectName.setError(errorString);
                mETSiteProjectName.requestFocus();
                return false;
            }

            if (mSPVertical.getSelectedItem().toString().equalsIgnoreCase("Select Vertical *")) {
                Utilities.showAlertDialog("Please Select Vertical", CreateManpowerActivity.this);
                return false;
            }
            if (mSPZone.getSelectedItem().toString().equalsIgnoreCase("Select Zone *")) {
                Utilities.showAlertDialog("Please Select Zone", CreateManpowerActivity.this);
                return false;
            }
            if (mSPBranch.getSelectedItem().toString().equalsIgnoreCase("Select Branch *")) {
                Utilities.showAlertDialog("Please Select Branch", CreateManpowerActivity.this);
                return false;
            }
            if (mETDate.getText().toString().trim().length() == 0) {

                String errorString = "Please Select Date";
                mETDate.setFocusable(true);
                mETDate.setFocusableInTouchMode(true);
                mETDate.setError(errorString);
                mETDate.requestFocus();
                return false;
            }
            if (mETHeadCount.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Head Count";
                mETHeadCount.setError(errorString);
                mETHeadCount.requestFocus();
                return false;
            }
            if (mETHeadCountContract.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Head Count Contract";
                mETHeadCountContract.setError(errorString);
                mETHeadCountContract.requestFocus();
                return false;
            }
            if (mETManHoursWorked.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Man Hours Worked";
                mETManHoursWorked.setError(errorString);
                mETManHoursWorked.requestFocus();
                return false;
            }
            if (mETManHoursWorkedContract.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Man Hours Worked Contract";
                mETManHoursWorkedContract.setError(errorString);
                mETManHoursWorkedContract.requestFocus();
                return false;
            }
            if (mETPPE.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter PPE";
                mETPPE.setError(errorString);
                mETPPE.requestFocus();
                return false;
            }
            if (mETPPE.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter PPE";
                mETPPE.setError(errorString);
                mETPPE.requestFocus();
                return false;
            }
            if (mETWorkAtHeight.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Work At Height";
                mETWorkAtHeight.setError(errorString);
                mETWorkAtHeight.requestFocus();
                return false;
            }
            if (mETEnvironment.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Environment";
                mETEnvironment.setError(errorString);
                mETEnvironment.requestFocus();
                return false;
            }
            if (mETLowRisk.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Low Risk";
                mETLowRisk.setError(errorString);
                mETLowRisk.requestFocus();
                return false;
            }
            if (mETMediumRisk.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Medium Risk";
                mETMediumRisk.setError(errorString);
                mETMediumRisk.requestFocus();
                return false;
            }
            if (mETHighRisk.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter High Risk";
                mETHighRisk.setError(errorString);
                mETHighRisk.requestFocus();
                return false;
            }
            if (mETCloseObservation.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Close Observation";
                mETCloseObservation.setError(errorString);
                mETCloseObservation.requestFocus();
                return false;
            }
            if (mETOpenObservation.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Open Observation";
                mETOpenObservation.setError(errorString);
                mETOpenObservation.requestFocus();
                return false;
            }
            if (mETProjectManagerName.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Project Manager Name";
                mETProjectManagerName.setError(errorString);
                mETProjectManagerName.requestFocus();
                return false;
            }
            if (mETProjectManagerMail.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Project Manager Email";
                mETProjectManagerMail.setError(errorString);
                mETProjectManagerMail.requestFocus();
                return false;
            }
            if (mETProjectManagerMobileno.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Project Manager MobileNo";
                mETProjectManagerMobileno.setError(errorString);
                mETProjectManagerMobileno.requestFocus();
                return false;
            }
            if (mETSafetyOfficerName.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Safety Officer Name";
                mETSafetyOfficerName.setError(errorString);
                mETSafetyOfficerName.requestFocus();
                return false;
            }
            if (mETSafetyOfficerMail.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Safety Officer Email";
                mETSafetyOfficerMail.setError(errorString);
                mETSafetyOfficerMail.requestFocus();
                return false;
            }
            if (mETSafetyOfficerMobileno.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Safety Officer MobileNo";
                mETSafetyOfficerMobileno.setError(errorString);
                mETSafetyOfficerMobileno.requestFocus();
                return false;
            }
            if (mETPDBUName.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter PB/BU Name";
                mETPDBUName.setError(errorString);
                mETPDBUName.requestFocus();
                return false;
            }
            if (mETPDBUMail.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter PB/BU Email";
                mETPDBUMail.setError(errorString);
                mETPDBUMail.requestFocus();
                return false;
            }
            if (mETPDBUMobileno.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter PB/BU MobileNo";
                mETPDBUMobileno.setError(errorString);
                mETPDBUMobileno.requestFocus();
                return false;
            }
            if (mETNoOfParticipents.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter No Of Participents";
                mETNoOfParticipents.setError(errorString);
                mETNoOfParticipents.requestFocus();
                return false;
            }
            if (mETNoOFSafetyMeetings.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter No Of Safety Meetings";
                mETNoOFSafetyMeetings.setError(errorString);
                mETNoOFSafetyMeetings.requestFocus();
                return false;
            }
            if (mETNoOfUnsafeActs.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter No Of Unsafe Acts";
                mETNoOfUnsafeActs.setError(errorString);
                mETNoOfUnsafeActs.requestFocus();
                return false;
            }
            if (mETNoOfUnsafeConditions.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter No Of Unsafe Conditions";
                mETNoOfUnsafeConditions.setError(errorString);
                mETNoOfUnsafeConditions.requestFocus();
                return false;
            }
           /* if (mETNoOfSheMeetings.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter No Of She Meetings";
                mETNoOfSheMeetings.setError(errorString);
                mETNoOfSheMeetings.requestFocus();
                return false;
            }*/
            if (mETFireExtinguishers.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Fire Extinguishers";
                mETFireExtinguishers.setError(errorString);
                mETFireExtinguishers.requestFocus();
                return false;
            }
            if (mETELCB.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter ELCB";
                mETELCB.setError(errorString);
                mETELCB.requestFocus();
                return false;
            }
            if (mETElectrical.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Electrical";
                mETElectrical.setError(errorString);
                mETElectrical.requestFocus();
                return false;
            }
            if (mETFullBodyHarness.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Full Body Harness";
                mETFullBodyHarness.setError(errorString);
                mETFullBodyHarness.requestFocus();
                return false;
            }
            if (mETPPES.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter PPES";
                mETPPES.setError(errorString);
                mETPPES.requestFocus();
                return false;
            }
            if (mETLiftingEquipment.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Lifting Equipment";
                mETLiftingEquipment.setError(errorString);
                mETLiftingEquipment.requestFocus();
                return false;
            }
            if (mETGrindingMachine.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Grinding Machine";
                mETGrindingMachine.setError(errorString);
                mETGrindingMachine.requestFocus();
                return false;
            }
            if (mETWeldingMachine.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Welding Machine";
                mETWeldingMachine.setError(errorString);
                mETWeldingMachine.requestFocus();
                return false;
            }
            if (mETGasCuttingMachine.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Gas Cutting Machine";
                mETGasCuttingMachine.setError(errorString);
                mETGasCuttingMachine.requestFocus();
                return false;
            }
            if (mETOfficeStore.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Office Store";
                mETOfficeStore.setError(errorString);
                mETOfficeStore.requestFocus();
                return false;
            }
            if (mETScaffoldLadder.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Scaffold Ladder";
                mETScaffoldLadder.setError(errorString);
                mETScaffoldLadder.requestFocus();
                return false;
            }
            if (mETThirdPartyVehicle.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Third Party Vehicle";
                mETThirdPartyVehicle.setError(errorString);
                mETThirdPartyVehicle.requestFocus();
                return false;
            }
            if (mETTotal.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Total";
                mETTotal.setError(errorString);
                mETTotal.requestFocus();
                return false;
            }
            if (mETFireDrillCount.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Fire Drill Count";
                mETFireDrillCount.setError(errorString);
                mETFireDrillCount.requestFocus();
                return false;
            }
            if (mETFireDrillDate.getText().toString().trim().length() == 0) {

                String errorString = "Please Select Fire Drill Date";
                mETFireDrillDate.setError(errorString);
                mETFireDrillDate.requestFocus();
                return false;
            }
            if (mETMockDrillCount.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Mock Drill Count";
                mETMockDrillCount.setError(errorString);
                mETMockDrillCount.requestFocus();
                return false;
            }
            if (mETMockDrillDate.getText().toString().trim().length() == 0) {

                String errorString = "Please Select Mock Drill Date";
                mETMockDrillDate.setError(errorString);
                mETMockDrillDate.requestFocus();
                return false;
            }
            if (mETEmergencyTotal.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Total";
                mETEmergencyTotal.setError(errorString);
                mETEmergencyTotal.requestFocus();
                return false;
            }
            if (mETElectricalFire.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Electrical Fire";
                mETElectricalFire.setError(errorString);
                mETElectricalFire.requestFocus();
                return false;
            }
            if (mETSlipTrip.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Slip Trip";
                mETSlipTrip.setError(errorString);
                mETSlipTrip.requestFocus();
                return false;
            }
            if (mETFallOfMaterial.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Fall Of Material";
                mETFallOfMaterial.setError(errorString);
                mETFallOfMaterial.requestFocus();
                return false;
            }
            if (mETEquipment.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Equipment";
                mETEquipment.setError(errorString);
                mETEquipment.requestFocus();
                return false;
            }
            if (mETCollision.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Collision";
                mETCollision.setError(errorString);
                mETCollision.requestFocus();
                return false;
            }
            if (mETToppling.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Toppling";
                mETToppling.setError(errorString);
                mETToppling.requestFocus();
                return false;
            }
            if (mETFloorOpening.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Floor Opening";
                mETFloorOpening.setError(errorString);
                mETFloorOpening.requestFocus();
                return false;
            }
            if (mETVehicleMovement.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Vehicle Movement";
                mETVehicleMovement.setError(errorString);
                mETVehicleMovement.requestFocus();
                return false;
            }
            if (mETNearmissTotal.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Total";
                mETNearmissTotal.setError(errorString);
                mETNearmissTotal.requestFocus();
                return false;
            }
            if (mETNSD.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter NSD";
                mETNSD.setError(errorString);
                mETNSD.requestFocus();
                return false;
            }
            if (mETRoad.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Road";
                mETRoad.setError(errorString);
                mETRoad.requestFocus();
                return false;
            }
            if (mETWED.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter WED";
                mETWED.setError(errorString);
                mETWED.requestFocus();
                return false;
            }
            if (mETHealth.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Health";
                mETHealth.setError(errorString);
                mETHealth.requestFocus();
                return false;
            }
            if (mETOther.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Other";
                mETOther.setError(errorString);
                mETOther.requestFocus();
                return false;
            }
            if (mETSafetyEventsTotal.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Total";
                mETSafetyEventsTotal.setError(errorString);
                mETSafetyEventsTotal.requestFocus();
                return false;
            }
            if (mETSrLeadership.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Sr. Leadership";
                mETSrLeadership.setError(errorString);
                mETSrLeadership.requestFocus();
                return false;
            }
            if (mETInternal.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Internal";
                mETInternal.setError(errorString);
                mETInternal.requestFocus();
                return false;
            }
            if (mETExternal.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter External";
                mETExternal.setError(errorString);
                mETExternal.requestFocus();
                return false;
            }
            if (mETAuditTotal.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Total";
                mETAuditTotal.setError(errorString);
                mETAuditTotal.requestFocus();
                return false;
            }
            if (mETSrManagement.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Sr. Management";
                mETSrManagement.setError(errorString);
                mETSrManagement.requestFocus();
                return false;
            }
            if (mETBUHeads.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter BU Heads";
                mETBUHeads.setError(errorString);
                mETBUHeads.requestFocus();
                return false;
            }
            if (mETSLLB.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter SLLB";
                mETSLLB.setError(errorString);
                mETSLLB.requestFocus();
                return false;
            }
            if (mETNoOfRewardGiven.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter No Of Reward Given";
                mETNoOfRewardGiven.setError(errorString);
                mETNoOfRewardGiven.requestFocus();
                return false;
            }
            if (mETNoOfSafetyViolationMemo.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter No Of Safety Violation Memo";
                mETNoOfSafetyViolationMemo.setError(errorString);
                mETNoOfSafetyViolationMemo.requestFocus();
                return false;
            }
            if (mETSafetyCommTotal.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Total";
                mETSafetyCommTotal.setError(errorString);
                mETSafetyCommTotal.requestFocus();
                return false;
            }
            if (mETStructureCollapse.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Structure Collapse";
                mETStructureCollapse.setError(errorString);
                mETStructureCollapse.requestFocus();
                return false;
            }
            if (mETHighPressureRelease.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter High Pressure Release";
                mETHighPressureRelease.setError(errorString);
                mETHighPressureRelease.requestFocus();
                return false;
            }
            if (mETHighLowTempExposure.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter High Low Temp Exposure";
                mETHighLowTempExposure.setError(errorString);
                mETHighLowTempExposure.requestFocus();
                return false;
            }
            if (mETBurstingMachine.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Bursting Machine";
                mETBurstingMachine.setError(errorString);
                mETBurstingMachine.requestFocus();
                return false;
            }
            if (mETDangerousTotal.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Total";
                mETDangerousTotal.setError(errorString);
                mETDangerousTotal.requestFocus();
                return false;
            }
            if (mETFatal.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Fatal";
                mETFatal.setError(errorString);
                mETFatal.requestFocus();
                return false;
            }
            if (mETLTI.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter LTI";
                mETLTI.setError(errorString);
                mETLTI.requestFocus();
                return false;
            }
            if (mETMTI.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter MTI";
                mETMTI.setError(errorString);
                mETMTI.requestFocus();
                return false;
            }
            if (mETFirstAid.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter First Aid";
                mETFirstAid.setError(errorString);
                mETFirstAid.requestFocus();
                return false;
            }
            if (mETInjuriesTotal.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Total";
                mETInjuriesTotal.setError(errorString);
                mETInjuriesTotal.requestFocus();
                return false;
            }

            if (mETLTIFR.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter LTIFR";
                mETLTIFR.setError(errorString);
                mETLTIFR.requestFocus();
                return false;
            }
            if (mETLTIFRPercent.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter LTIFR %";
                mETLTIFRPercent.setError(errorString);
                mETLTIFRPercent.requestFocus();
                return false;
            }
            if (mETMTIFR.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter MTIFR";
                mETMTIFR.setError(errorString);
                mETMTIFR.requestFocus();
                return false;
            }
            if (mETMTIFRPercent.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter MTIFR %";
                mETMTIFRPercent.setError(errorString);
                mETMTIFRPercent.requestFocus();
                return false;
            }
            if (mETAIFR.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter AIFR";
                mETAIFR.setError(errorString);
                mETAIFR.requestFocus();
                return false;
            }
            if (mETAIFRPercent.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter AIFR %";
                mETAIFRPercent.setError(errorString);
                mETAIFRPercent.requestFocus();
                return false;
            }
            if (mETInjuriesRateTotal.getText().toString().trim().length() == 0) {

                String errorString = "Please Enter Total";
                mETInjuriesRateTotal.setError(errorString);
                mETInjuriesRateTotal.requestFocus();
                return false;
            }

        } catch (Exception e) {
            e.getMessage();
            Log.d("Exception in Login", "" + e.getMessage());
        }
        return true;
    }


}
