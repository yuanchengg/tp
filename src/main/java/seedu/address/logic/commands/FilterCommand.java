package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ENDDATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_HEALTHSERVICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STARTDATE;
import static seedu.address.model.filteredappointment.FilteredAppointment.APPOINTMENT_COMPARATOR;

import java.util.List;
import java.util.TreeSet;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.appointmentdatefilter.AppointmentDateFilter;
import seedu.address.model.filteredappointment.FilteredAppointment;
import seedu.address.model.patient.Appt;
import seedu.address.model.patient.Patient;

/**
 * Filters the patients based on their appointment dates and health services.
 */
public class FilterCommand extends Command {

    public static final String COMMAND_WORD = "filter";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Filters patients based on their appointment dates and"
            + " healthservices. Parameters : "
            + PREFIX_STARTDATE + "START_DATE "
            + PREFIX_ENDDATE + "END_DATE "
            + "[" + PREFIX_HEALTHSERVICE + "HEALTHSERVICE] ...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_STARTDATE + "2024-08-30 "
            + PREFIX_ENDDATE + "2024-11-30 "
            + PREFIX_HEALTHSERVICE + "VACCINATION";

    public static final String MESSAGE_SUCCESS = "List of patients sorted based on their appointment dates";

    private final AppointmentDateFilter dateFilter;

    public FilterCommand(AppointmentDateFilter dateFilter) {
        this.dateFilter = dateFilter;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        TreeSet<FilteredAppointment> filteredAppts = new TreeSet<>(APPOINTMENT_COMPARATOR);

        List<Patient> patientList = model.getFilteredPatientList();
        for (Patient patient : patientList) {
            for (Appt appt : patient.getAppts()) {
                if (appt.isBetweenDatesAndMatchService(dateFilter)) {
                    FilteredAppointment filteredAppt = new FilteredAppointment(appt, patient);
                    filteredAppts.add(filteredAppt);
                }
            }
        }
        model.setFilteredAppts(filteredAppts);
        return new CommandResult(MESSAGE_SUCCESS, "appts");
    }
}