package oof;

import java.util.InputMismatchException;

import oof.command.AddAssessmentCommand;
import oof.command.AddAssignmentCommand;
import oof.command.AddDeadlineCommand;
import oof.command.AddEventCommand;
import oof.command.AddLessonCommand;
import oof.command.AddModuleCommand;
import oof.command.AddSemesterCommand;
import oof.command.AddToDoCommand;
import oof.command.CalendarCommand;
import oof.command.Command;
import oof.command.CompleteCommand;
import oof.command.DeleteLessonCommand;
import oof.command.DeleteModuleCommand;
import oof.command.DeleteSemesterCommand;
import oof.command.DeleteTaskCommand;
import oof.command.ExitCommand;
import oof.command.FindCommand;
import oof.command.FreeCommand;
import oof.command.HelpCommand;
import oof.command.ListCommand;
import oof.command.PauseTrackerCommand;
import oof.command.RecurringCommand;
import oof.command.ScheduleCommand;
import oof.command.SelectModuleCommand;
import oof.command.SelectSemesterCommand;
import oof.command.SnoozeCommand;
import oof.command.StartTrackerCommand;
import oof.command.StopTrackerCommand;
import oof.command.SummaryCommand;
import oof.command.ThresholdCommand;
import oof.command.ViewAllModuleCommand;
import oof.command.ViewAllSemesterCommand;
import oof.command.ViewLessonCommand;
import oof.command.ViewSelectedModuleCommand;
import oof.command.ViewSelectedSemesterCommand;
import oof.command.ViewTrackerCommand;
import oof.command.ViewWeekCommand;
import oof.exception.OofException;

/**
 * Represents a parser to process the commands inputted by the user.
 */
public class CommandParser {

    private static Ui ui;
    private static final int LENGTH_COMMAND_ONLY = 1;
    private static final int LENGTH_COMMAND_AND_TASK = 2;
    private static final int INDEX_ARGUMENT_COMMAND = 0;
    private static final int INDEX_ARGUMENT_TASK_NUMBER = 1;
    private static final int INDEX_ARGUMENT_COUNT = 2;
    private static final int INDEX_TASK_TYPE = 1;
    private static final String DELIMITER = "||";

    /**
     * Parses the input given by user and calls specific Commands
     * after checking the validity of the input.
     *
     * @param line Command inputted by user.
     * @return Command based on the user input.
     * @throws OofException Catches invalid commands given by user.
     */
    public static Command parse(String line) throws OofException {
        if (containsIllegalInput(line)) {
            throw new OofException("Your command contains illegal input!");
        }
        String[] argumentArray = line.split(" ");
        switch (argumentArray[INDEX_ARGUMENT_COMMAND]) {
        case "bye":
            return new ExitCommand();
        case "list":
            return new ListCommand();
        case "help":
            line = line.replaceFirst("help", "").trim();
            return new HelpCommand(line);
        case "done":
            return parseDone(argumentArray, line);
        case "todo":
            line = line.replaceFirst("todo", "").trim();
            return new AddToDoCommand(line);
        case "assignment":
            line = line.replaceFirst("assignment", "").trim();
            return new AddAssignmentCommand(line);
        case "deadline":
            line = line.replaceFirst("deadline", "").trim();
            return new AddDeadlineCommand(line);
        case "assessment":
            line = line.replaceFirst("assessment", "").trim();
            return new AddAssessmentCommand(line);
        case "event":
            line = line.replaceFirst("event", "").trim();
            return new AddEventCommand(line);
        case "delete":
            return parseDelete(argumentArray, line);
        case "find":
            return new FindCommand(line);
        case "snooze":
            return parseSnooze(argumentArray, line);
        case "schedule":
            line = line.replaceFirst("schedule", "").trim();
            return new ScheduleCommand(line);
        case "summary":
            return new SummaryCommand();
        case "recurring":
            return parseRecurring(argumentArray);
        case "calendar":
            return new CalendarCommand(argumentArray);
        case "viewweek":
            return new ViewWeekCommand(argumentArray);
        case "free":
            line = line.replaceFirst("free", "").trim();
            return new FreeCommand(line);
        case "start":
            line = line.replaceFirst("start", "").trim();
            return new StartTrackerCommand(line);
        case "stop":
            line = line.replaceFirst("stop", "").trim();
            return new StopTrackerCommand(line);
        case "pause":
            line = line.replaceFirst("pause", "").trim();
            return new PauseTrackerCommand(line);
        case "viewtracker":
            line = line.replaceFirst("viewtracker", "").trim();
            return new ViewTrackerCommand(line);
        case "threshold":
            line = line.replaceFirst("threshold", "").trim();
            return new ThresholdCommand(line);
        case "semester":
            return parseSemester(argumentArray, line);
        case "module":
            return parseModule(argumentArray, line);
        case "lesson":
            return parseLesson(argumentArray, line);
        default:
            throw new OofException("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }

    /**
     * Checks if user input contains illegal characters.
     * @param line User input.
     * @return Returns true if user input contains illegal characters, false otherwise.
     */
    private static boolean containsIllegalInput(String line) {
        if (line.contains(DELIMITER)) {
            return true;
        }
        return false;
    }

    /**
     * Parses input if the user input starts with done.
     *
     * @param argumentArray Command inputted by user in string array format.
     * @param line          Command inputted by user in string format.
     * @return Returns an instance of CompleteCommand if the parameters are valid.
     * @throws OofException Throws exception if the parameters are invalid.
     */
    private static Command parseDone(String[] argumentArray, String line) throws OofException {
        if (argumentArray.length == LENGTH_COMMAND_ONLY) {
            throw new OofException("OOPS!!! Please enter a number!");
        }
        try {
            int completeIndex = Integer.parseInt(line.replaceFirst("done", "").trim()) - 1;
            return new CompleteCommand(completeIndex);
        } catch (NumberFormatException e) {
            throw new OofException("OOPS!!! Please enter a valid number!");
        }
    }

    /**
     * Parses input if the user input starts with delete.
     *
     * @param argumentArray Command inputted by user in string array format.
     * @param line          Command inputted by user in string format.
     * @return Returns an instance of DeleteCommand if the parameters are valid.
     * @throws OofException Throws exception if the parameters are invalid.
     */
    private static Command parseDelete(String[] argumentArray, String line) throws OofException {
        if (argumentArray.length == LENGTH_COMMAND_ONLY) {
            throw new OofException("OOPS!!! Please enter a number!");
        } else if (argumentArray.length == LENGTH_COMMAND_AND_TASK) {
            try {
                int deleteIndex = Integer.parseInt(line.replaceFirst("delete", "").trim()) - 1;
                return new DeleteTaskCommand(deleteIndex);
            } catch (NumberFormatException e) {
                throw new OofException("OOPS!!! Please enter a valid number!");
            }
        } else {
            throw new OofException("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }

    /**
     * Parses input if the user input starts with semester.
     *
     * @param argumentArray Command inputted by user in string array format.
     * @param line          Command inputted by user in string format.
     * @return Returns relevant Semester Commands if the parameters are valid.
     * @throws OofException Throws exception if the parameters are invalid.
     */
    private static Command parseSemester(String[] argumentArray, String line) throws OofException {
        if (argumentArray.length == LENGTH_COMMAND_ONLY) {
            return new ViewSelectedSemesterCommand();
        } else if (argumentArray[INDEX_TASK_TYPE].equals("/view")) {
            return new ViewAllSemesterCommand();
        } else if (argumentArray[INDEX_TASK_TYPE].equals("/add")) {
            line = line.replaceFirst("semester /add", "").trim();
            return new AddSemesterCommand(line);
        } else if (argumentArray[INDEX_TASK_TYPE].equals("/delete")) {
            line = line.replaceFirst("semester /delete", "").trim();
            try {
                int deleteIndex = Integer.parseInt(line) - 1;
                return new DeleteSemesterCommand(deleteIndex);
            } catch (NumberFormatException e) {
                throw new OofException("OOPS!!! Please enter a valid number!");
            }
        } else if (argumentArray[INDEX_TASK_TYPE].equals("/select")) {
            line = line.replaceFirst("semester /select", "").trim();
            return new SelectSemesterCommand(line);
        } else {
            throw new OofException("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }

    /**
     * Parses input if the user input starts with module.
     *
     * @param argumentArray Command inputted by user in string array format.
     * @param line          Command inputted by user in string format.
     * @return Returns relevant Module Commands if the parameters are valid.
     * @throws OofException Throws exception if the parameters are invalid.
     */
    private static Command parseModule(String[] argumentArray, String line) throws OofException {
        if (argumentArray.length == LENGTH_COMMAND_ONLY) {
            return new ViewSelectedModuleCommand();
        } else if (argumentArray[INDEX_TASK_TYPE].equals("/view")) {
            return new ViewAllModuleCommand();
        } else if (argumentArray[INDEX_TASK_TYPE].equals("/add")) {
            line = line.replaceFirst("module /add", "").trim();
            return new AddModuleCommand(line);
        } else if (argumentArray[INDEX_TASK_TYPE].equals("/delete")) {
            line = line.replaceFirst("module /delete", "").trim();
            try {
                int deleteIndex = Integer.parseInt(line) - 1;
                return new DeleteModuleCommand(deleteIndex);
            } catch (NumberFormatException e) {
                throw new OofException("OOPS!!! Please enter a valid number!");
            }
        } else if (argumentArray[INDEX_TASK_TYPE].equals("/select")) {
            line = line.replaceFirst("module /select", "").trim();
            return new SelectModuleCommand(line);
        } else {
            throw new OofException("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }

    /**
     * Parses input if the user input starts with lesson.
     *
     * @param argumentArray Command inputted by user in string array format.
     * @param line          Command inputted by user in string format.
     * @return Returns relevant Lesson Commands if the parameters are valid.
     * @throws OofException Throws exception if the parameters are invalid.
     */
    private static Command parseLesson(String[] argumentArray, String line) throws OofException {
        if (argumentArray.length == LENGTH_COMMAND_ONLY) {
            return new ViewLessonCommand();
        } else if (argumentArray[INDEX_TASK_TYPE].equals("/add")) {
            line = line.replaceFirst("lesson /add", "").trim();
            return new AddLessonCommand(line);
        } else if (argumentArray[INDEX_TASK_TYPE].equals("/delete")) {
            line = line.replaceFirst("lesson /delete", "").trim();
            try {
                int deleteIndex = Integer.parseInt(line) - 1;
                return new DeleteLessonCommand(deleteIndex);
            } catch (NumberFormatException e) {
                throw new OofException("OOPS!!! Please enter a valid number!");
            }
        } else {
            throw new OofException("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }

    /**
     * Parses input if the user input starts with snooze.
     *
     * @param argumentArray Command inputted by user in string array format.
     * @param line          Command inputted by user in string format.
     * @return Returns an instance of SnoozeCommand if the parameters are valid.
     * @throws OofException Throws exception if the parameters are invalid.
     */
    private static Command parseSnooze(String[] argumentArray, String line) throws OofException {
        if (argumentArray.length == LENGTH_COMMAND_ONLY) {
            throw new OofException("OOPS!!! Please enter a number!");
        }
        try {
            int snoozeIndex = Integer.parseInt(line.replaceFirst("snooze", "").trim()) - 1;
            return new SnoozeCommand(snoozeIndex);
        } catch (NumberFormatException e) {
            throw new OofException("OOPS!!! Please enter a valid number!");
        }
    }

    /**
     * Parses input if the user input starts with recurring.
     *
     * @param argumentArray Command inputted by user in string array format.
     * @return Returns an instance of RecurringCommand if the parameters are valid.
     * @throws OofException Throws exception if the parameters are invalid.
     */
    private static Command parseRecurring(String[] argumentArray) throws OofException {
        if (argumentArray.length == LENGTH_COMMAND_ONLY) {
            throw new OofException("OOPS!!! Please enter the task number and number of recurrences!");
        } else if (argumentArray.length == LENGTH_COMMAND_AND_TASK) {
            throw new OofException("OOPS!!! Please enter the number of recurrences!");
        }
        try {
            int recurringIndex = Integer.parseInt(argumentArray[INDEX_ARGUMENT_TASK_NUMBER].trim()) - 1;
            int recurringCount = Integer.parseInt(argumentArray[INDEX_ARGUMENT_COUNT].trim());
            ui = new Ui();
            ui.printRecurringOptions();
            int recurringFrequency = ui.scanInt();
            return new RecurringCommand(recurringIndex, recurringCount, recurringFrequency);
        } catch (NumberFormatException e) {
            throw new OofException("OOPS!!! Please enter valid numbers!");
        } catch (InputMismatchException e) {
            throw new OofException("OOPS!!! Please enter a valid number!");
        }
    }
}
