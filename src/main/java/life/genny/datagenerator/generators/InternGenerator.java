package life.genny.datagenerator.generators;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;

import life.genny.datagenerator.Entities;
import life.genny.datagenerator.Regex;
import life.genny.datagenerator.SpecialAttributes;
import life.genny.datagenerator.utils.DataFakerCustomUtils;
import life.genny.datagenerator.utils.DataFakerUtils;
import life.genny.qwandaq.attribute.EntityAttribute;
import life.genny.qwandaq.constants.Prefix;
import life.genny.qwandaq.entity.BaseEntity;

/**
 * Generate all important attributes for DEF_INTERN and DEF_INTERNSHIP
 * 
 * @author Amrizal Fajar
 */
@ApplicationScoped
public class InternGenerator extends CustomFakeDataGenerator {

    private final List<String> WORK_DAYS = new ArrayList<>(
            Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"));

    @Inject
    Logger log;

    /**
     * Initialize needed parameter to generate each {@link EntityAttribute}
     * 
     * @param entity Initialized {@link BaseEntity}
     * @return {@link BaseEntity} with all important attributes filled in
     */
    @Override
    public BaseEntity generateImpl(String defCode, BaseEntity entity) {
        String superName = DataFakerCustomUtils.generateName() + " " +
                DataFakerCustomUtils.generateName();
        Map<String, LocalDateTime> prevPeriod = generatePeriod();
        int daysPerWeek = DataFakerUtils.randInt(1, 5);

        List<String> daysStripped = new ArrayList<>();
        while (daysStripped.size() < daysPerWeek) {
            String day = DataFakerUtils.randItemFromList(WORK_DAYS);
            if (daysStripped.contains(day))
                continue;
            daysStripped.add(day);
        }
        Collections.sort(daysStripped, Comparator.comparing(WORK_DAYS::indexOf));

        //        for (EntityAttribute ea : entity.findPrefixEntityAttributes(Prefix.ATT_)) {

        // ATT_ has been removed in DataFakerService.createBaseEntity, so there is no ATT_ in the attribute anymore
        for (EntityAttribute ea : entity.getBaseEntityAttributes()){
            if (ea.getValue() != null && !"".equals(ea.getValue())) continue;

            Object newObj = runGenerator(defCode, ea, defCode, superName,
                    prevPeriod.get("start").toString(), prevPeriod.get("end").toString(),
                    String.valueOf(daysPerWeek), StringUtils.join(daysStripped));
            if (newObj != null)
                ea.setValue(newObj);
        }
        return entity;
    }

    /**
     * Start Generating {@link EntityAttribute} based on entity code
     * 
     * @param attributeCode The attribute code
     * @param regex         The regex pattern
     * @param args          The additional parameters needed
     * @return Generated {@link EntityAttribute} value
     */
    @Override
    Object runGeneratorImpl(String attributeCode, String regex, String... args) {
        String defCode = args[0];
        String superName = args[1];
        String startPrevPeriod = args[2];
        String endPrevPeriod = args[3];
        String daysPerWeek = args[4];
        String daysStripped = args[5];
        return switch (defCode) {
            case Entities.DEF_INTERN -> generateIntern(attributeCode, startPrevPeriod,
                    endPrevPeriod, daysPerWeek, daysStripped);
            case Entities.DEF_INTERNSHIP -> generateInternship(attributeCode, superName,
                    daysPerWeek, daysStripped);
            default -> null;
        };
    }

    /**
     * Generate {@link EntityAttribute} of DEF_INTERN
     * 
     * @param attributeCode The attribute code
     * @param className     The name of the class
     * @return Generated {@link EntityAttribute} value
     */
    Object generateIntern(String attributeCode, String... args) {
        LocalDateTime startPrevPeriod = LocalDateTime.parse(args[0]);
        LocalDateTime endPrevPeriod = LocalDateTime.parse(args[1]);
        int daysPerWeek = Integer.parseInt(args[2]);
        String daysStripped = StringUtils.substringBetween(args[3], "[", "]");
        return switch (attributeCode) {
            case SpecialAttributes.PRI_CV:
                yield "YOU NEED TO ABSOLUTELY CHANGE THIS";

            case SpecialAttributes.PRI_AGENT_NAME:
            case SpecialAttributes.PRI_ADDED_BY:
                yield DataFakerCustomUtils.generateName();

            case SpecialAttributes.PRI_EMAIL_ADDITIONAL:
                yield DataFakerCustomUtils.generateEmail();

            case SpecialAttributes.PRI_STUDENT_ID:
                yield DataFakerUtils.randStringFromRegex(Regex.STUDENT_ID_REGEX);

            case SpecialAttributes.PRI_PREV_PERIOD:
                DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("dd-MMM-yy");
                yield startPrevPeriod.format(dtFormatter).toString() + " - " +
                        endPrevPeriod.format(dtFormatter);

            case SpecialAttributes.PRI_CAREER_OBJ:
                yield DataFakerCustomUtils.generateDescriptiveHTMLTag("p",
                        DataFakerUtils.randInt(1, 4));

            case SpecialAttributes.PRI_DAYS_PER_WEEK:
                yield String.valueOf(daysPerWeek);

            case SpecialAttributes.PRI_WHICH_DAYS_STRIPPED:
                yield daysStripped;

            case SpecialAttributes.LNK_PREV_PERIOD:
                yield "{\"startDate\": \"%s\", \"endDate\": \"%s\"}".formatted(
                        startPrevPeriod.toString(), endPrevPeriod.toString());

            case SpecialAttributes.LNK_DAYS_PER_WEEK:
                yield "[\"SEL_" + convertNumberToWord(daysPerWeek).toUpperCase() + "\"]";

            case SpecialAttributes.LNK_WHICH_DAYS:
                List<String> whichDays = Arrays.asList(daysStripped.split(", ")).stream()
                        .map(day -> "SEL_WHICH_DAYS_" + day.toUpperCase())
                        .collect(Collectors.toList());
                yield "[" + String.join(", ", whichDays) + "]";

            case SpecialAttributes.LNK_INTERNSHIP_DURATION:
                yield "[SEL_DURATION_12_WEEKS]";

            default:
                yield null;
        };
    }

    /**
     * Generate {@link EntityAttribute} of DEF_INTERNSHIP
     * 
     * @param attributeCode The code of the attribute
     * @param name          The supervisor name
     * @return Generated {@link EntityAttribute} value
     */
    Object generateInternship(String attributeCode, String... args) {
        String name = args[0];
        int daysPerWeek = Integer.parseInt(args[1]);
        String daysStripped = StringUtils.substringBetween(args[2], "[", "]");

        return switch (attributeCode) {
            case SpecialAttributes.PRI_SUPER_MOBILE:
                yield DataFakerCustomUtils.generatePhoneNumber();

            case SpecialAttributes.PRI_SUPER_NAME:
                yield name;

            case SpecialAttributes.PRI_SUPER_EMAIL:
                String[] names = name.split(" ");
                yield DataFakerCustomUtils.generateEmail(names[0], names[1]);

            case SpecialAttributes.PRI_ASSOC_NUM_INTERNS:
            case SpecialAttributes.PRI_NO_OF_INTERNS:
                yield 1;

            case SpecialAttributes.PRI_INTERNSHIP_DETAILS:
            case SpecialAttributes.PRI_SPECIFIC_LEARNING_OUTCOMES:
                yield DataFakerCustomUtils.generateDescriptiveHTMLTag();

            case SpecialAttributes.PRI_BASE_LEARNING_OUTCOMES:
            case SpecialAttributes.PRI_ROLES_AND_RESPONSIBILITIES:
            case SpecialAttributes.PRI_CAREER_OBJ:
                yield DataFakerCustomUtils.generateDescriptiveHTMLTag("p",
                        DataFakerUtils.randInt(1, 4));

            case SpecialAttributes.PRI_WHICH_DAYS_STRIPPED:
                yield daysStripped;

            case SpecialAttributes.PRI_DAYS_PER_WEEK:
                yield String.valueOf(daysPerWeek);

            case SpecialAttributes.LNK_INTERNSHIP_TYPE:
            case SpecialAttributes.LNK_WORKSITE_SELECT:
            case SpecialAttributes.LNK_INTERVIEW_TYPE:
                yield "[\"" + DataFakerCustomUtils.generateSelection() + "\"]";

            case SpecialAttributes.LNK_NO_OF_INTERNS:
                yield "[\"SEL_NO_OF_INTERNS_" +
                        convertNumberToWord(1).toUpperCase() + "\"]";

            case SpecialAttributes.LNK_DAYS_PER_WEEK:
                yield "[\"SEL_" + convertNumberToWord(daysPerWeek).toUpperCase() + "\"]";

            case SpecialAttributes.LNK_WHICH_DAYS:
                List<String> whichDays = Arrays.asList(daysStripped.split(", ")).stream()
                        .map(day -> "SEL_WHICH_DAYS_" + day.toUpperCase())
                        .collect(Collectors.toList());
                yield "[" + String.join(", ", whichDays) + "]";

            default:
                yield null;
        };
    }

    private Map<String, LocalDateTime> generatePeriod() {
        LocalDate startDate = DataFakerUtils.randDateTime().toLocalDate();
        LocalDate endDate = DataFakerUtils.randDateTime(startDate).toLocalDate();
        Map<String, LocalDateTime> period = new HashMap<>(2);
        period.put("start", startDate.atStartOfDay());
        period.put("end", endDate.atStartOfDay());
        return period;
    }

    private String convertNumberToWord(int num) {
        if (num > 10)
            throw new IllegalArgumentException("This function cannot handle number above 10 or less than 0.");
        List<String> numbers = new ArrayList<>(Arrays.asList(
                "Zero", "One", "Two", "Three", "Four", "Five",
                "Six", "Seven", "Eight", "Nine", "Ten"));
        return numbers.get(num);
    }
}
