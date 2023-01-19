package com.ags.voltassafety.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreateManPowerResult {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("updatedBy")
    @Expose
    private String updatedBy;
    @SerializedName("updatedon")
    @Expose
    private String updatedon;
    @SerializedName("misdate")
    @Expose
    private String misdate;
    @SerializedName("staffHeadcount")
    @Expose
    private Integer staffHeadcount;
    @SerializedName("contractHeadcount")
    @Expose
    private Integer contractHeadcount;
    @SerializedName("staffWorkedHours")
    @Expose
    private Integer staffWorkedHours;
    @SerializedName("contractWorkedHours")
    @Expose
    private Integer contractWorkedHours;
    @SerializedName("noOfParticipantsInToolBoxTalk")
    @Expose
    private Integer noOfParticipantsInToolBoxTalk;
    @SerializedName("noOfSafetyMeetings")
    @Expose
    private Integer noOfSafetyMeetings;
    @SerializedName("noOfSafetyObservationsUnsafeAct")
    @Expose
    private Integer noOfSafetyObservationsUnsafeAct;
    @SerializedName("noOfSafetyObservationsUnsafeCondition")
    @Expose
    private Integer noOfSafetyObservationsUnsafeCondition;
    @SerializedName("noOfSafetyInspectionsFireExtinguishers")
    @Expose
    private Integer noOfSafetyInspectionsFireExtinguishers;
    @SerializedName("noOfSafetyInspectionsElcb")
    @Expose
    private Integer noOfSafetyInspectionsElcb;
    @SerializedName("noOfSafetyInspectionsElectrical")
    @Expose
    private Integer noOfSafetyInspectionsElectrical;
    @SerializedName("noOfSafetyInspectionsFullBodyHarness")
    @Expose
    private Integer noOfSafetyInspectionsFullBodyHarness;
    @SerializedName("noOfSafetyInspectionsPppes")
    @Expose
    private Integer noOfSafetyInspectionsPppes;
    @SerializedName("noOfSafetyInspectionsLiftingEquipment")
    @Expose
    private Integer noOfSafetyInspectionsLiftingEquipment;
    @SerializedName("noOfSafetyInspectionsGrindingMachine")
    @Expose
    private Integer noOfSafetyInspectionsGrindingMachine;
    @SerializedName("noOfSafetyInspectionsWeldingMachine")
    @Expose
    private Integer noOfSafetyInspectionsWeldingMachine;
    @SerializedName("noOfSafetyInspectionsGasCuttingMachine")
    @Expose
    private Integer noOfSafetyInspectionsGasCuttingMachine;
    @SerializedName("noOfSafetyInspectionsOfficeStore")
    @Expose
    private Integer noOfSafetyInspectionsOfficeStore;
    @SerializedName("noOfSafetyInspectionsScaffoldLadder")
    @Expose
    private Integer noOfSafetyInspectionsScaffoldLadder;
    @SerializedName("noOfSafetyInspectionsThirdPartyVehicle")
    @Expose
    private Integer noOfSafetyInspectionsThirdPartyVehicle;
    @SerializedName("emergencyResponseFireDrillDate")
    @Expose
    private String emergencyResponseFireDrillDate;
    @SerializedName("emergencyResponseMockDrillDate")
    @Expose
    private String emergencyResponseMockDrillDate;
    @SerializedName("nearMissElectricalFire")
    @Expose
    private Integer nearMissElectricalFire;
    @SerializedName("nearMissSlipTrip")
    @Expose
    private Integer nearMissSlipTrip;
    @SerializedName("nearMissFallOfMaterial")
    @Expose
    private Integer nearMissFallOfMaterial;
    @SerializedName("nearMissEquipment")
    @Expose
    private Integer nearMissEquipment;
    @SerializedName("nearMissCollision")
    @Expose
    private Integer nearMissCollision;
    @SerializedName("nearMissToppling")
    @Expose
    private Integer nearMissToppling;
    @SerializedName("nearMissFloorOpening")
    @Expose
    private Integer nearMissFloorOpening;
    @SerializedName("nearMissVehicleMovement")
    @Expose
    private Integer nearMissVehicleMovement;
    @SerializedName("safetyEventsNsd")
    @Expose
    private Integer safetyEventsNsd;
    @SerializedName("safetyEventsRoad")
    @Expose
    private Integer safetyEventsRoad;
    @SerializedName("safetyEventsWed")
    @Expose
    private Integer safetyEventsWed;
    @SerializedName("safetyEventsHealth")
    @Expose
    private Integer safetyEventsHealth;
    @SerializedName("safetyEventsOther")
    @Expose
    private Integer safetyEventsOther;
    @SerializedName("noOfAuditsSrLeadership")
    @Expose
    private Integer noOfAuditsSrLeadership;
    @SerializedName("noOfAuditsInternal")
    @Expose
    private Integer noOfAuditsInternal;
    @SerializedName("noOfAuditsExternal")
    @Expose
    private Integer noOfAuditsExternal;
    @SerializedName("safetyCommunicationsSrManagement")
    @Expose
    private Integer safetyCommunicationsSrManagement;
    @SerializedName("safetyCommunicationsBuheads")
    @Expose
    private Integer safetyCommunicationsBuheads;
    @SerializedName("safetyCommunicationsSllb")
    @Expose
    private Integer safetyCommunicationsSllb;
    @SerializedName("noOfRewardGiven")
    @Expose
    private Integer noOfRewardGiven;
    @SerializedName("noOfSafetViolationMemoIssued")
    @Expose
    private Integer noOfSafetViolationMemoIssued;
    @SerializedName("dangerousOccurrencesStructureCollapse")
    @Expose
    private Integer dangerousOccurrencesStructureCollapse;
    @SerializedName("dangerousOccurrencesHighPreassureRelease")
    @Expose
    private Integer dangerousOccurrencesHighPreassureRelease;
    @SerializedName("dangerousOccurencesHighLowTempExposure")
    @Expose
    private Integer dangerousOccurencesHighLowTempExposure;
    @SerializedName("dangerousOccurencesBurstiingOfMachine")
    @Expose
    private Integer dangerousOccurencesBurstiingOfMachine;
    @SerializedName("noOfInjuriesFatal")
    @Expose
    private Integer noOfInjuriesFatal;
    @SerializedName("noOfInjuriesLti")
    @Expose
    private Integer noOfInjuriesLti;
    @SerializedName("noOfInjuriesMti")
    @Expose
    private Integer noOfInjuriesMti;
    @SerializedName("noOfInjuriesFirstAid")
    @Expose
    private Integer noOfInjuriesFirstAid;
    @SerializedName("injuryRateLtifr")
    @Expose
    private Integer injuryRateLtifr;
    @SerializedName("injuryRateMtifr")
    @Expose
    private Integer injuryRateMtifr;
    @SerializedName("injuryRateAfr")
    @Expose
    private Integer injuryRateAfr;
    @SerializedName("noOfStaffSheinducted")
    @Expose
    private Integer noOfStaffSheinducted;
    @SerializedName("noOfContractSheinducted")
    @Expose
    private Integer noOfContractSheinducted;
    @SerializedName("projectName")
    @Expose
    private String projectName;
    @SerializedName("vertical")
    @Expose
    private String vertical;
    @SerializedName("zone")
    @Expose
    private String zone;
    @SerializedName("branch")
    @Expose
    private String branch;
    @SerializedName("managerContact")
    @Expose
    private String managerContact;
    @SerializedName("safetyOfficerContact")
    @Expose
    private String safetyOfficerContact;
    @SerializedName("pdorBucontact")
    @Expose
    private String pdorBucontact;
    @SerializedName("managerEmail")
    @Expose
    private String managerEmail;
    @SerializedName("safetyOfficerEmail")
    @Expose
    private String safetyOfficerEmail;
    @SerializedName("pdorBuemail")
    @Expose
    private String pdorBuemail;
    @SerializedName("managerName")
    @Expose
    private String managerName;
    @SerializedName("safetyOfficerName")
    @Expose
    private String safetyOfficerName;
    @SerializedName("pdorBuname")
    @Expose
    private String pdorBuname;
    @SerializedName("noOfShemeetings")
    @Expose
    private Integer noOfShemeetings;
    @SerializedName("injuryRateLtifrPer")
    @Expose
    private Integer injuryRateLtifrPer;
    @SerializedName("injuryRateMtifrPer")
    @Expose
    private Integer injuryRateMtifrPer;
    @SerializedName("injuryRateAfrPer")
    @Expose
    private Integer injuryRateAfrPer;
    @SerializedName("emergencyResponseFireDrillCount")
    @Expose
    private Integer emergencyResponseFireDrillCount;
    @SerializedName("emergencyResponseMockDrillCount")
    @Expose
    private Integer emergencyResponseMockDrillCount;
    @SerializedName("soeppe")
    @Expose
    private Integer soeppe;
    @SerializedName("soeworkatHeight")
    @Expose
    private Integer soeworkatHeight;
    @SerializedName("soeenvironment")
    @Expose
    private Integer soeenvironment;
    @SerializedName("soelowRisk")
    @Expose
    private Integer soelowRisk;
    @SerializedName("soemediumRisk")
    @Expose
    private Integer soemediumRisk;
    @SerializedName("soehighRisk")
    @Expose
    private Integer soehighRisk;
    @SerializedName("soecloseObservation")
    @Expose
    private Integer soecloseObservation;
    @SerializedName("soeopenObservation")
    @Expose
    private Integer soeopenObservation;
    @SerializedName("internalOtherName")
    @Expose
    private String internalOtherName;
    @SerializedName("externalOtherName")
    @Expose
    private String externalOtherName;
    @SerializedName("external")
    @Expose
    private List<External> external = null;
    @SerializedName("internal")
    @Expose
    private List<Internal> internal = null;
    @SerializedName("contInjRateLiftPer")
    @Expose
    private Integer contInjRateLiftPer;
    @SerializedName("contInjRateMtiPer")
    @Expose
    private Integer contInjRateMtiPer;
    @SerializedName("contInjRateAfrper")
    @Expose
    private Integer contInjRateAfrper;
    @SerializedName("noOfsuggestionsOther")
    @Expose
    private Integer noOfsuggestionsOther;
    @SerializedName("noOfSuggestionsOtherName")
    @Expose
    private String noOfSuggestionsOtherName;
    @SerializedName("contPff")
    @Expose
    private Integer contPff;
    @SerializedName("contWorkatHeight")
    @Expose
    private Integer contWorkatHeight;
    @SerializedName("contEvnironmment")
    @Expose
    private Integer contEvnironmment;
    @SerializedName("contLowatRisk")
    @Expose
    private Integer contLowatRisk;
    @SerializedName("contHighRisk")
    @Expose
    private Integer contHighRisk;
    @SerializedName("contMediumRisk")
    @Expose
    private Integer contMediumRisk;
    @SerializedName("contCloseObservation")
    @Expose
    private Integer contCloseObservation;
    @SerializedName("contOpneObservation")
    @Expose
    private Integer contOpneObservation;
    @SerializedName("contOther")
    @Expose
    private Integer contOther;
    @SerializedName("contOthername")
    @Expose
    private String contOthername;
    @SerializedName("dangOccOther")
    @Expose
    private Integer dangOccOther;
    @SerializedName("danOccOtherName")
    @Expose
    private String danOccOtherName;
    @SerializedName("emergencyResponOthers")
    @Expose
    private Integer emergencyResponOthers;
    @SerializedName("emergencyResponOtherName")
    @Expose
    private String emergencyResponOtherName;
    @SerializedName("contFatal")
    @Expose
    private Integer contFatal;
    @SerializedName("contLti")
    @Expose
    private Integer contLti;
    @SerializedName("contMti")
    @Expose
    private Integer contMti;
    @SerializedName("contFirstAid")
    @Expose
    private Integer contFirstAid;
    @SerializedName("nearMissOther")
    @Expose
    private Integer nearMissOther;
    @SerializedName("nearMissOthername")
    @Expose
    private String nearMissOthername;
    @SerializedName("auditOther")
    @Expose
    private Integer auditOther;
    @SerializedName("auditOtherName")
    @Expose
    private String auditOtherName;
    @SerializedName("noOfsafetyInspectionStore")
    @Expose
    private Integer noOfsafetyInspectionStore;
    @SerializedName("noOfsafetyInspectionLadder")
    @Expose
    private Integer noOfsafetyInspectionLadder;
    @SerializedName("noOfsafetyInspectionOther")
    @Expose
    private Integer noOfsafetyInspectionOther;
    @SerializedName("noOfsafetyInspectionOtherName")
    @Expose
    private String noOfsafetyInspectionOtherName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedon() {
        return updatedon;
    }

    public void setUpdatedon(String updatedon) {
        this.updatedon = updatedon;
    }

    public String getMisdate() {
        return misdate;
    }

    public void setMisdate(String misdate) {
        this.misdate = misdate;
    }

    public Integer getStaffHeadcount() {
        return staffHeadcount;
    }

    public void setStaffHeadcount(Integer staffHeadcount) {
        this.staffHeadcount = staffHeadcount;
    }

    public Integer getContractHeadcount() {
        return contractHeadcount;
    }

    public void setContractHeadcount(Integer contractHeadcount) {
        this.contractHeadcount = contractHeadcount;
    }

    public Integer getStaffWorkedHours() {
        return staffWorkedHours;
    }

    public void setStaffWorkedHours(Integer staffWorkedHours) {
        this.staffWorkedHours = staffWorkedHours;
    }

    public Integer getContractWorkedHours() {
        return contractWorkedHours;
    }

    public void setContractWorkedHours(Integer contractWorkedHours) {
        this.contractWorkedHours = contractWorkedHours;
    }

    public Integer getNoOfParticipantsInToolBoxTalk() {
        return noOfParticipantsInToolBoxTalk;
    }

    public void setNoOfParticipantsInToolBoxTalk(Integer noOfParticipantsInToolBoxTalk) {
        this.noOfParticipantsInToolBoxTalk = noOfParticipantsInToolBoxTalk;
    }

    public Integer getNoOfSafetyMeetings() {
        return noOfSafetyMeetings;
    }

    public void setNoOfSafetyMeetings(Integer noOfSafetyMeetings) {
        this.noOfSafetyMeetings = noOfSafetyMeetings;
    }

    public Integer getNoOfSafetyObservationsUnsafeAct() {
        return noOfSafetyObservationsUnsafeAct;
    }

    public void setNoOfSafetyObservationsUnsafeAct(Integer noOfSafetyObservationsUnsafeAct) {
        this.noOfSafetyObservationsUnsafeAct = noOfSafetyObservationsUnsafeAct;
    }

    public Integer getNoOfSafetyObservationsUnsafeCondition() {
        return noOfSafetyObservationsUnsafeCondition;
    }

    public void setNoOfSafetyObservationsUnsafeCondition(Integer noOfSafetyObservationsUnsafeCondition) {
        this.noOfSafetyObservationsUnsafeCondition = noOfSafetyObservationsUnsafeCondition;
    }

    public Integer getNoOfSafetyInspectionsFireExtinguishers() {
        return noOfSafetyInspectionsFireExtinguishers;
    }

    public void setNoOfSafetyInspectionsFireExtinguishers(Integer noOfSafetyInspectionsFireExtinguishers) {
        this.noOfSafetyInspectionsFireExtinguishers = noOfSafetyInspectionsFireExtinguishers;
    }

    public Integer getNoOfSafetyInspectionsElcb() {
        return noOfSafetyInspectionsElcb;
    }

    public void setNoOfSafetyInspectionsElcb(Integer noOfSafetyInspectionsElcb) {
        this.noOfSafetyInspectionsElcb = noOfSafetyInspectionsElcb;
    }

    public Integer getNoOfSafetyInspectionsElectrical() {
        return noOfSafetyInspectionsElectrical;
    }

    public void setNoOfSafetyInspectionsElectrical(Integer noOfSafetyInspectionsElectrical) {
        this.noOfSafetyInspectionsElectrical = noOfSafetyInspectionsElectrical;
    }

    public Integer getNoOfSafetyInspectionsFullBodyHarness() {
        return noOfSafetyInspectionsFullBodyHarness;
    }

    public void setNoOfSafetyInspectionsFullBodyHarness(Integer noOfSafetyInspectionsFullBodyHarness) {
        this.noOfSafetyInspectionsFullBodyHarness = noOfSafetyInspectionsFullBodyHarness;
    }

    public Integer getNoOfSafetyInspectionsPppes() {
        return noOfSafetyInspectionsPppes;
    }

    public void setNoOfSafetyInspectionsPppes(Integer noOfSafetyInspectionsPppes) {
        this.noOfSafetyInspectionsPppes = noOfSafetyInspectionsPppes;
    }

    public Integer getNoOfSafetyInspectionsLiftingEquipment() {
        return noOfSafetyInspectionsLiftingEquipment;
    }

    public void setNoOfSafetyInspectionsLiftingEquipment(Integer noOfSafetyInspectionsLiftingEquipment) {
        this.noOfSafetyInspectionsLiftingEquipment = noOfSafetyInspectionsLiftingEquipment;
    }

    public Integer getNoOfSafetyInspectionsGrindingMachine() {
        return noOfSafetyInspectionsGrindingMachine;
    }

    public void setNoOfSafetyInspectionsGrindingMachine(Integer noOfSafetyInspectionsGrindingMachine) {
        this.noOfSafetyInspectionsGrindingMachine = noOfSafetyInspectionsGrindingMachine;
    }

    public Integer getNoOfSafetyInspectionsWeldingMachine() {
        return noOfSafetyInspectionsWeldingMachine;
    }

    public void setNoOfSafetyInspectionsWeldingMachine(Integer noOfSafetyInspectionsWeldingMachine) {
        this.noOfSafetyInspectionsWeldingMachine = noOfSafetyInspectionsWeldingMachine;
    }

    public Integer getNoOfSafetyInspectionsGasCuttingMachine() {
        return noOfSafetyInspectionsGasCuttingMachine;
    }

    public void setNoOfSafetyInspectionsGasCuttingMachine(Integer noOfSafetyInspectionsGasCuttingMachine) {
        this.noOfSafetyInspectionsGasCuttingMachine = noOfSafetyInspectionsGasCuttingMachine;
    }

    public Integer getNoOfSafetyInspectionsOfficeStore() {
        return noOfSafetyInspectionsOfficeStore;
    }

    public void setNoOfSafetyInspectionsOfficeStore(Integer noOfSafetyInspectionsOfficeStore) {
        this.noOfSafetyInspectionsOfficeStore = noOfSafetyInspectionsOfficeStore;
    }

    public Integer getNoOfSafetyInspectionsScaffoldLadder() {
        return noOfSafetyInspectionsScaffoldLadder;
    }

    public void setNoOfSafetyInspectionsScaffoldLadder(Integer noOfSafetyInspectionsScaffoldLadder) {
        this.noOfSafetyInspectionsScaffoldLadder = noOfSafetyInspectionsScaffoldLadder;
    }

    public Integer getNoOfSafetyInspectionsThirdPartyVehicle() {
        return noOfSafetyInspectionsThirdPartyVehicle;
    }

    public void setNoOfSafetyInspectionsThirdPartyVehicle(Integer noOfSafetyInspectionsThirdPartyVehicle) {
        this.noOfSafetyInspectionsThirdPartyVehicle = noOfSafetyInspectionsThirdPartyVehicle;
    }

    public String getEmergencyResponseFireDrillDate() {
        return emergencyResponseFireDrillDate;
    }

    public void setEmergencyResponseFireDrillDate(String emergencyResponseFireDrillDate) {
        this.emergencyResponseFireDrillDate = emergencyResponseFireDrillDate;
    }

    public String getEmergencyResponseMockDrillDate() {
        return emergencyResponseMockDrillDate;
    }

    public void setEmergencyResponseMockDrillDate(String emergencyResponseMockDrillDate) {
        this.emergencyResponseMockDrillDate = emergencyResponseMockDrillDate;
    }

    public Integer getNearMissElectricalFire() {
        return nearMissElectricalFire;
    }

    public void setNearMissElectricalFire(Integer nearMissElectricalFire) {
        this.nearMissElectricalFire = nearMissElectricalFire;
    }

    public Integer getNearMissSlipTrip() {
        return nearMissSlipTrip;
    }

    public void setNearMissSlipTrip(Integer nearMissSlipTrip) {
        this.nearMissSlipTrip = nearMissSlipTrip;
    }

    public Integer getNearMissFallOfMaterial() {
        return nearMissFallOfMaterial;
    }

    public void setNearMissFallOfMaterial(Integer nearMissFallOfMaterial) {
        this.nearMissFallOfMaterial = nearMissFallOfMaterial;
    }

    public Integer getNearMissEquipment() {
        return nearMissEquipment;
    }

    public void setNearMissEquipment(Integer nearMissEquipment) {
        this.nearMissEquipment = nearMissEquipment;
    }

    public Integer getNearMissCollision() {
        return nearMissCollision;
    }

    public void setNearMissCollision(Integer nearMissCollision) {
        this.nearMissCollision = nearMissCollision;
    }

    public Integer getNearMissToppling() {
        return nearMissToppling;
    }

    public void setNearMissToppling(Integer nearMissToppling) {
        this.nearMissToppling = nearMissToppling;
    }

    public Integer getNearMissFloorOpening() {
        return nearMissFloorOpening;
    }

    public void setNearMissFloorOpening(Integer nearMissFloorOpening) {
        this.nearMissFloorOpening = nearMissFloorOpening;
    }

    public Integer getNearMissVehicleMovement() {
        return nearMissVehicleMovement;
    }

    public void setNearMissVehicleMovement(Integer nearMissVehicleMovement) {
        this.nearMissVehicleMovement = nearMissVehicleMovement;
    }

    public Integer getSafetyEventsNsd() {
        return safetyEventsNsd;
    }

    public void setSafetyEventsNsd(Integer safetyEventsNsd) {
        this.safetyEventsNsd = safetyEventsNsd;
    }

    public Integer getSafetyEventsRoad() {
        return safetyEventsRoad;
    }

    public void setSafetyEventsRoad(Integer safetyEventsRoad) {
        this.safetyEventsRoad = safetyEventsRoad;
    }

    public Integer getSafetyEventsWed() {
        return safetyEventsWed;
    }

    public void setSafetyEventsWed(Integer safetyEventsWed) {
        this.safetyEventsWed = safetyEventsWed;
    }

    public Integer getSafetyEventsHealth() {
        return safetyEventsHealth;
    }

    public void setSafetyEventsHealth(Integer safetyEventsHealth) {
        this.safetyEventsHealth = safetyEventsHealth;
    }

    public Integer getSafetyEventsOther() {
        return safetyEventsOther;
    }

    public void setSafetyEventsOther(Integer safetyEventsOther) {
        this.safetyEventsOther = safetyEventsOther;
    }

    public Integer getNoOfAuditsSrLeadership() {
        return noOfAuditsSrLeadership;
    }

    public void setNoOfAuditsSrLeadership(Integer noOfAuditsSrLeadership) {
        this.noOfAuditsSrLeadership = noOfAuditsSrLeadership;
    }

    public Integer getNoOfAuditsInternal() {
        return noOfAuditsInternal;
    }

    public void setNoOfAuditsInternal(Integer noOfAuditsInternal) {
        this.noOfAuditsInternal = noOfAuditsInternal;
    }

    public Integer getNoOfAuditsExternal() {
        return noOfAuditsExternal;
    }

    public void setNoOfAuditsExternal(Integer noOfAuditsExternal) {
        this.noOfAuditsExternal = noOfAuditsExternal;
    }

    public Integer getSafetyCommunicationsSrManagement() {
        return safetyCommunicationsSrManagement;
    }

    public void setSafetyCommunicationsSrManagement(Integer safetyCommunicationsSrManagement) {
        this.safetyCommunicationsSrManagement = safetyCommunicationsSrManagement;
    }

    public Integer getSafetyCommunicationsBuheads() {
        return safetyCommunicationsBuheads;
    }

    public void setSafetyCommunicationsBuheads(Integer safetyCommunicationsBuheads) {
        this.safetyCommunicationsBuheads = safetyCommunicationsBuheads;
    }

    public Integer getSafetyCommunicationsSllb() {
        return safetyCommunicationsSllb;
    }

    public void setSafetyCommunicationsSllb(Integer safetyCommunicationsSllb) {
        this.safetyCommunicationsSllb = safetyCommunicationsSllb;
    }

    public Integer getNoOfRewardGiven() {
        return noOfRewardGiven;
    }

    public void setNoOfRewardGiven(Integer noOfRewardGiven) {
        this.noOfRewardGiven = noOfRewardGiven;
    }

    public Integer getNoOfSafetViolationMemoIssued() {
        return noOfSafetViolationMemoIssued;
    }

    public void setNoOfSafetViolationMemoIssued(Integer noOfSafetViolationMemoIssued) {
        this.noOfSafetViolationMemoIssued = noOfSafetViolationMemoIssued;
    }

    public Integer getDangerousOccurrencesStructureCollapse() {
        return dangerousOccurrencesStructureCollapse;
    }

    public void setDangerousOccurrencesStructureCollapse(Integer dangerousOccurrencesStructureCollapse) {
        this.dangerousOccurrencesStructureCollapse = dangerousOccurrencesStructureCollapse;
    }

    public Integer getDangerousOccurrencesHighPreassureRelease() {
        return dangerousOccurrencesHighPreassureRelease;
    }

    public void setDangerousOccurrencesHighPreassureRelease(Integer dangerousOccurrencesHighPreassureRelease) {
        this.dangerousOccurrencesHighPreassureRelease = dangerousOccurrencesHighPreassureRelease;
    }

    public Integer getDangerousOccurencesHighLowTempExposure() {
        return dangerousOccurencesHighLowTempExposure;
    }

    public void setDangerousOccurencesHighLowTempExposure(Integer dangerousOccurencesHighLowTempExposure) {
        this.dangerousOccurencesHighLowTempExposure = dangerousOccurencesHighLowTempExposure;
    }

    public Integer getDangerousOccurencesBurstiingOfMachine() {
        return dangerousOccurencesBurstiingOfMachine;
    }

    public void setDangerousOccurencesBurstiingOfMachine(Integer dangerousOccurencesBurstiingOfMachine) {
        this.dangerousOccurencesBurstiingOfMachine = dangerousOccurencesBurstiingOfMachine;
    }

    public Integer getNoOfInjuriesFatal() {
        return noOfInjuriesFatal;
    }

    public void setNoOfInjuriesFatal(Integer noOfInjuriesFatal) {
        this.noOfInjuriesFatal = noOfInjuriesFatal;
    }

    public Integer getNoOfInjuriesLti() {
        return noOfInjuriesLti;
    }

    public void setNoOfInjuriesLti(Integer noOfInjuriesLti) {
        this.noOfInjuriesLti = noOfInjuriesLti;
    }

    public Integer getNoOfInjuriesMti() {
        return noOfInjuriesMti;
    }

    public void setNoOfInjuriesMti(Integer noOfInjuriesMti) {
        this.noOfInjuriesMti = noOfInjuriesMti;
    }

    public Integer getNoOfInjuriesFirstAid() {
        return noOfInjuriesFirstAid;
    }

    public void setNoOfInjuriesFirstAid(Integer noOfInjuriesFirstAid) {
        this.noOfInjuriesFirstAid = noOfInjuriesFirstAid;
    }

    public Integer getInjuryRateLtifr() {
        return injuryRateLtifr;
    }

    public void setInjuryRateLtifr(Integer injuryRateLtifr) {
        this.injuryRateLtifr = injuryRateLtifr;
    }

    public Integer getInjuryRateMtifr() {
        return injuryRateMtifr;
    }

    public void setInjuryRateMtifr(Integer injuryRateMtifr) {
        this.injuryRateMtifr = injuryRateMtifr;
    }

    public Integer getInjuryRateAfr() {
        return injuryRateAfr;
    }

    public void setInjuryRateAfr(Integer injuryRateAfr) {
        this.injuryRateAfr = injuryRateAfr;
    }

    public Integer getNoOfStaffSheinducted() {
        return noOfStaffSheinducted;
    }

    public void setNoOfStaffSheinducted(Integer noOfStaffSheinducted) {
        this.noOfStaffSheinducted = noOfStaffSheinducted;
    }

    public Integer getNoOfContractSheinducted() {
        return noOfContractSheinducted;
    }

    public void setNoOfContractSheinducted(Integer noOfContractSheinducted) {
        this.noOfContractSheinducted = noOfContractSheinducted;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getVertical() {
        return vertical;
    }

    public void setVertical(String vertical) {
        this.vertical = vertical;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getManagerContact() {
        return managerContact;
    }

    public void setManagerContact(String managerContact) {
        this.managerContact = managerContact;
    }

    public String getSafetyOfficerContact() {
        return safetyOfficerContact;
    }

    public void setSafetyOfficerContact(String safetyOfficerContact) {
        this.safetyOfficerContact = safetyOfficerContact;
    }

    public String getPdorBucontact() {
        return pdorBucontact;
    }

    public void setPdorBucontact(String pdorBucontact) {
        this.pdorBucontact = pdorBucontact;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public String getSafetyOfficerEmail() {
        return safetyOfficerEmail;
    }

    public void setSafetyOfficerEmail(String safetyOfficerEmail) {
        this.safetyOfficerEmail = safetyOfficerEmail;
    }

    public String getPdorBuemail() {
        return pdorBuemail;
    }

    public void setPdorBuemail(String pdorBuemail) {
        this.pdorBuemail = pdorBuemail;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getSafetyOfficerName() {
        return safetyOfficerName;
    }

    public void setSafetyOfficerName(String safetyOfficerName) {
        this.safetyOfficerName = safetyOfficerName;
    }

    public String getPdorBuname() {
        return pdorBuname;
    }

    public void setPdorBuname(String pdorBuname) {
        this.pdorBuname = pdorBuname;
    }

    public Integer getNoOfShemeetings() {
        return noOfShemeetings;
    }

    public void setNoOfShemeetings(Integer noOfShemeetings) {
        this.noOfShemeetings = noOfShemeetings;
    }

    public Integer getInjuryRateLtifrPer() {
        return injuryRateLtifrPer;
    }

    public void setInjuryRateLtifrPer(Integer injuryRateLtifrPer) {
        this.injuryRateLtifrPer = injuryRateLtifrPer;
    }

    public Integer getInjuryRateMtifrPer() {
        return injuryRateMtifrPer;
    }

    public void setInjuryRateMtifrPer(Integer injuryRateMtifrPer) {
        this.injuryRateMtifrPer = injuryRateMtifrPer;
    }

    public Integer getInjuryRateAfrPer() {
        return injuryRateAfrPer;
    }

    public void setInjuryRateAfrPer(Integer injuryRateAfrPer) {
        this.injuryRateAfrPer = injuryRateAfrPer;
    }

    public Integer getEmergencyResponseFireDrillCount() {
        return emergencyResponseFireDrillCount;
    }

    public void setEmergencyResponseFireDrillCount(Integer emergencyResponseFireDrillCount) {
        this.emergencyResponseFireDrillCount = emergencyResponseFireDrillCount;
    }

    public Integer getEmergencyResponseMockDrillCount() {
        return emergencyResponseMockDrillCount;
    }

    public void setEmergencyResponseMockDrillCount(Integer emergencyResponseMockDrillCount) {
        this.emergencyResponseMockDrillCount = emergencyResponseMockDrillCount;
    }

    public Integer getSoeppe() {
        return soeppe;
    }

    public void setSoeppe(Integer soeppe) {
        this.soeppe = soeppe;
    }

    public Integer getSoeworkatHeight() {
        return soeworkatHeight;
    }

    public void setSoeworkatHeight(Integer soeworkatHeight) {
        this.soeworkatHeight = soeworkatHeight;
    }

    public Integer getSoeenvironment() {
        return soeenvironment;
    }

    public void setSoeenvironment(Integer soeenvironment) {
        this.soeenvironment = soeenvironment;
    }

    public Integer getSoelowRisk() {
        return soelowRisk;
    }

    public void setSoelowRisk(Integer soelowRisk) {
        this.soelowRisk = soelowRisk;
    }

    public Integer getSoemediumRisk() {
        return soemediumRisk;
    }

    public void setSoemediumRisk(Integer soemediumRisk) {
        this.soemediumRisk = soemediumRisk;
    }

    public Integer getSoehighRisk() {
        return soehighRisk;
    }

    public void setSoehighRisk(Integer soehighRisk) {
        this.soehighRisk = soehighRisk;
    }

    public Integer getSoecloseObservation() {
        return soecloseObservation;
    }

    public void setSoecloseObservation(Integer soecloseObservation) {
        this.soecloseObservation = soecloseObservation;
    }

    public Integer getSoeopenObservation() {
        return soeopenObservation;
    }

    public void setSoeopenObservation(Integer soeopenObservation) {
        this.soeopenObservation = soeopenObservation;
    }

    public String getInternalOtherName() {
        return internalOtherName;
    }

    public void setInternalOtherName(String internalOtherName) {
        this.internalOtherName = internalOtherName;
    }

    public String getExternalOtherName() {
        return externalOtherName;
    }

    public void setExternalOtherName(String externalOtherName) {
        this.externalOtherName = externalOtherName;
    }

    public List<External> getExternal() {
        return external;
    }

    public void setExternal(List<External> external) {
        this.external = external;
    }

    public List<Internal> getInternal() {
        return internal;
    }

    public void setInternal(List<Internal> internal) {
        this.internal = internal;
    }

    public Integer getContInjRateLiftPer() {
        return contInjRateLiftPer;
    }

    public void setContInjRateLiftPer(Integer contInjRateLiftPer) {
        this.contInjRateLiftPer = contInjRateLiftPer;
    }

    public Integer getContInjRateMtiPer() {
        return contInjRateMtiPer;
    }

    public void setContInjRateMtiPer(Integer contInjRateMtiPer) {
        this.contInjRateMtiPer = contInjRateMtiPer;
    }

    public Integer getContInjRateAfrper() {
        return contInjRateAfrper;
    }

    public void setContInjRateAfrper(Integer contInjRateAfrper) {
        this.contInjRateAfrper = contInjRateAfrper;
    }

    public Integer getNoOfsuggestionsOther() {
        return noOfsuggestionsOther;
    }

    public void setNoOfsuggestionsOther(Integer noOfsuggestionsOther) {
        this.noOfsuggestionsOther = noOfsuggestionsOther;
    }

    public String getNoOfSuggestionsOtherName() {
        return noOfSuggestionsOtherName;
    }

    public void setNoOfSuggestionsOtherName(String noOfSuggestionsOtherName) {
        this.noOfSuggestionsOtherName = noOfSuggestionsOtherName;
    }

    public Integer getContPff() {
        return contPff;
    }

    public void setContPff(Integer contPff) {
        this.contPff = contPff;
    }

    public Integer getContWorkatHeight() {
        return contWorkatHeight;
    }

    public void setContWorkatHeight(Integer contWorkatHeight) {
        this.contWorkatHeight = contWorkatHeight;
    }

    public Integer getContEvnironmment() {
        return contEvnironmment;
    }

    public void setContEvnironmment(Integer contEvnironmment) {
        this.contEvnironmment = contEvnironmment;
    }

    public Integer getContLowatRisk() {
        return contLowatRisk;
    }

    public void setContLowatRisk(Integer contLowatRisk) {
        this.contLowatRisk = contLowatRisk;
    }

    public Integer getContHighRisk() {
        return contHighRisk;
    }

    public void setContHighRisk(Integer contHighRisk) {
        this.contHighRisk = contHighRisk;
    }

    public Integer getContMediumRisk() {
        return contMediumRisk;
    }

    public void setContMediumRisk(Integer contMediumRisk) {
        this.contMediumRisk = contMediumRisk;
    }

    public Integer getContCloseObservation() {
        return contCloseObservation;
    }

    public void setContCloseObservation(Integer contCloseObservation) {
        this.contCloseObservation = contCloseObservation;
    }

    public Integer getContOpneObservation() {
        return contOpneObservation;
    }

    public void setContOpneObservation(Integer contOpneObservation) {
        this.contOpneObservation = contOpneObservation;
    }

    public Integer getContOther() {
        return contOther;
    }

    public void setContOther(Integer contOther) {
        this.contOther = contOther;
    }

    public String getContOthername() {
        return contOthername;
    }

    public void setContOthername(String contOthername) {
        this.contOthername = contOthername;
    }

    public Integer getDangOccOther() {
        return dangOccOther;
    }

    public void setDangOccOther(Integer dangOccOther) {
        this.dangOccOther = dangOccOther;
    }

    public String getDanOccOtherName() {
        return danOccOtherName;
    }

    public void setDanOccOtherName(String danOccOtherName) {
        this.danOccOtherName = danOccOtherName;
    }

    public Integer getEmergencyResponOthers() {
        return emergencyResponOthers;
    }

    public void setEmergencyResponOthers(Integer emergencyResponOthers) {
        this.emergencyResponOthers = emergencyResponOthers;
    }

    public String getEmergencyResponOtherName() {
        return emergencyResponOtherName;
    }

    public void setEmergencyResponOtherName(String emergencyResponOtherName) {
        this.emergencyResponOtherName = emergencyResponOtherName;
    }

    public Integer getContFatal() {
        return contFatal;
    }

    public void setContFatal(Integer contFatal) {
        this.contFatal = contFatal;
    }

    public Integer getContLti() {
        return contLti;
    }

    public void setContLti(Integer contLti) {
        this.contLti = contLti;
    }

    public Integer getContMti() {
        return contMti;
    }

    public void setContMti(Integer contMti) {
        this.contMti = contMti;
    }

    public Integer getContFirstAid() {
        return contFirstAid;
    }

    public void setContFirstAid(Integer contFirstAid) {
        this.contFirstAid = contFirstAid;
    }

    public Integer getNearMissOther() {
        return nearMissOther;
    }

    public void setNearMissOther(Integer nearMissOther) {
        this.nearMissOther = nearMissOther;
    }

    public String getNearMissOthername() {
        return nearMissOthername;
    }

    public void setNearMissOthername(String nearMissOthername) {
        this.nearMissOthername = nearMissOthername;
    }

    public Integer getAuditOther() {
        return auditOther;
    }

    public void setAuditOther(Integer auditOther) {
        this.auditOther = auditOther;
    }

    public String getAuditOtherName() {
        return auditOtherName;
    }

    public void setAuditOtherName(String auditOtherName) {
        this.auditOtherName = auditOtherName;
    }

    public Integer getNoOfsafetyInspectionStore() {
        return noOfsafetyInspectionStore;
    }

    public void setNoOfsafetyInspectionStore(Integer noOfsafetyInspectionStore) {
        this.noOfsafetyInspectionStore = noOfsafetyInspectionStore;
    }

    public Integer getNoOfsafetyInspectionLadder() {
        return noOfsafetyInspectionLadder;
    }

    public void setNoOfsafetyInspectionLadder(Integer noOfsafetyInspectionLadder) {
        this.noOfsafetyInspectionLadder = noOfsafetyInspectionLadder;
    }

    public Integer getNoOfsafetyInspectionOther() {
        return noOfsafetyInspectionOther;
    }

    public void setNoOfsafetyInspectionOther(Integer noOfsafetyInspectionOther) {
        this.noOfsafetyInspectionOther = noOfsafetyInspectionOther;
    }

    public String getNoOfsafetyInspectionOtherName() {
        return noOfsafetyInspectionOtherName;
    }

    public void setNoOfsafetyInspectionOtherName(String noOfsafetyInspectionOtherName) {
        this.noOfsafetyInspectionOtherName = noOfsafetyInspectionOtherName;
    }

}
