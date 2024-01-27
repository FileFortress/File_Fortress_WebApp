package edu.sltc.vaadin.models;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * @author Nuyun-Kalamullage
 * @IDE IntelliJ IDEA
 * @date 23/11/2023
 * @package edu.sltc.vaadin.models
 * @project_Name File_Fortress_WebApp
 */
// Example ExamModel.java
public class ExamModel {
    private static ExamModel instance;
    public static boolean serverIsRunning = false; // Track server state
    private String moduleCode;
    private String moduleName;
    private String moduleDescription;
    private String lateSubmission;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String ExamPaperName;

    private ExamModel() {
        // private constructor to enforce Singleton pattern
    }

    public static ExamModel getInstance() {
        if (instance == null) {
            instance = new ExamModel();
        }
        return instance;
    }

    // Getter and setter methods for exam details
    public Optional<String> getModuleCode() {
        return Optional.ofNullable(moduleCode);
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public Optional<String> getModuleName() {
        return Optional.ofNullable(moduleName);
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Optional<String> getModuleDescription() {
        return Optional.ofNullable(moduleDescription);
    }

    public void setModuleDescription(String moduleDescription) {
        this.moduleDescription = moduleDescription;
    }

    public Optional<String> getLateSubmission() {
        return Optional.ofNullable(lateSubmission);
    }

    public void setLateSubmission(String lateSubmission) {
        this.lateSubmission = lateSubmission;
    }

    public Optional<LocalTime> getStartTime() {
        if (startTime != null)
            return Optional.ofNullable(startTime.toLocalTime());
        else
            return Optional.empty();
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Optional<LocalTime> getEndTime() {
        if (endTime != null)
            return Optional.ofNullable(endTime.toLocalTime());
        else
            return Optional.empty();
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Optional<String> getExamPaperName() {
        return Optional.ofNullable(ExamPaperName);
    }

    public void setExamPaperName(String examPaperName) {
        ExamPaperName = examPaperName;
    }
    public Optional<LocalDateTime> getEndDateTime(){
        if (startTime != null && endTime != null) {
            LocalDateTime modifiedDateTime = endTime;
            Duration duration = Duration.between(startTime, endTime);
            if (duration.isNegative()) {
                // If duration is negative, return the next day's LocalDate with the end time
                modifiedDateTime = endTime.plusDays(1);
            }
            // If duration is not negative, calculate the end time normally
            return Optional.of(modifiedDateTime.plusMinutes(getLateSubmissionValue().orElse(0)));
        } else {
            return Optional.empty();
        }
    }

    public void resetInstance(){
        instance = new ExamModel();
    }
    public Optional<Integer> getLateSubmissionValue() {
        if (lateSubmission != null) {
            return switch (lateSubmission) {
                case "10 Minutes" -> Optional.of(10);
                case "15 Minutes" -> Optional.of(15);
                case "20 Minutes" -> Optional.of(20);
                case "25 Minutes" -> Optional.of(25);
                case "30 Minutes" -> Optional.of(30);
                default -> Optional.empty();
            };
        } else {
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        return "ExamModel{" +
                "moduleCode='" + moduleCode + '\'' +
                ", moduleName='" + moduleName + '\'' +
                ", moduleDescription='" + moduleDescription + '\'' +
                ", lateSubmission='" + lateSubmission + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", ExamPaperName='" + ExamPaperName + '\'' +
                '}';
    }
}

