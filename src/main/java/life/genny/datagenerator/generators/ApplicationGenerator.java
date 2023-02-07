package life.genny.datagenerator.generators;

import java.util.*;

import javax.enterprise.context.ApplicationScoped;

import life.genny.datagenerator.Entities;
import org.apache.commons.lang3.StringUtils;

import life.genny.datagenerator.Regex;
import life.genny.datagenerator.SpecialAttributes;
import life.genny.datagenerator.utils.DataFakerCustomUtils;
import life.genny.datagenerator.utils.DataFakerUtils;
import life.genny.qwandaq.attribute.EntityAttribute;
import life.genny.qwandaq.constants.Prefix;
import life.genny.qwandaq.entity.BaseEntity;

@ApplicationScoped
public class ApplicationGenerator extends CustomFakeDataGenerator {

    private final List<String> WORK_DAYS = new ArrayList<>(
            Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"));

    @Override
    BaseEntity generateImpl(String defCode, BaseEntity entity) {
        String superName = DataFakerCustomUtils.generateName() + " " +
                DataFakerCustomUtils.generateName();
        int daysPerWeek = DataFakerUtils.randInt(1, 5);

        List<String> daysStripped = new ArrayList<>();
        while (daysStripped.size() < daysPerWeek) {
            String day = DataFakerUtils.randItemFromList(WORK_DAYS);
            if (daysStripped.contains(day))
                continue;
            daysStripped.add(day);
        }
        daysStripped.sort(Comparator.comparing(WORK_DAYS::indexOf));

//        for (EntityAttribute ea : entity.findPrefixEntityAttributes(Prefix.ATT_)) {

        // ATT_ has been removed in DataFakerService.createBaseEntity, so there is no ATT_ in the attribute anymore
        for (EntityAttribute ea : entity.getBaseEntityAttributes()) {
            Object newObj = runGenerator(defCode, ea, superName, String.valueOf(daysPerWeek),
                    StringUtils.join(daysStripped));
            if (newObj != null)
                ea.setValue(newObj);
        }
        return entity;
    }

    @Override
    Object runGeneratorImpl(String attributeCode, String regex, String... args) {
        String superName = args[0];
        int daysPerWeek = Integer.parseInt(args[1]);

//        log.info("ApplicationGenerator:runGeneratorImpl("+attributeCode+","+regex+","+ Arrays.toString(args) +")");
//        if (tempEntityMap == null)
//            tempEntityMap = new HashMap<>(10);

        return switch (attributeCode) {
            case SpecialAttributes.PRI_BASE_LEARNING_OUTCOMES:
            case SpecialAttributes.PRI_ROLES_AND_RESPONSIBILITIES:
                yield DataFakerCustomUtils.generateHTMLTag(
                        DataFakerCustomUtils.generateDescriptiveHTMLTag("li",
                                DataFakerUtils.randInt(1, 4)),
                        "ul");

            case SpecialAttributes.PRI_SUPER_NAME:
                yield superName;

            case SpecialAttributes.PRI_SUPER_EMAIL:
                String[] names = superName.split(" ");
                yield DataFakerCustomUtils.generateEmail(names[0], names[1]);

            case SpecialAttributes.PRI_AGENT_NAME:
            case SpecialAttributes.PRI_SUPER_JOB_TITLE:
                yield DataFakerCustomUtils.generateName();

            case SpecialAttributes.PRI_SUPER_MOBILE:
                yield DataFakerCustomUtils.generatePhoneNumber();

            case SpecialAttributes.PRI_INTERN_STUDENT_ID:
                yield DataFakerUtils.randStringFromRegex(Regex.STUDENT_ID_REGEX);

            case SpecialAttributes.PRI_DAYS_PER_WEEK:
                yield String.valueOf(daysPerWeek);

            case SpecialAttributes.PRI_OUTCOME_LIFE_REP_NAME:
                yield DataFakerCustomUtils.generateName() + " " +
                        DataFakerCustomUtils.generateName();

            case SpecialAttributes.PRI_HOURS_PER_WEEK:
                yield Double.valueOf(daysPerWeek * 8);

            case SpecialAttributes.PRI_INTERNSHIP_DETAILS:
            case SpecialAttributes.PRI_APPLICANT_CODE:
                yield "";

            case SpecialAttributes.LNK_NO_OF_INTERNS:
                yield "[\"SEL_NO_OF_INTERNS_" +
                        convertNumberToWord(1).toUpperCase() + "\"]";

            case SpecialAttributes.LNK_OCCUPATION:
            case SpecialAttributes.LNK_INTERVIEW_TYPE:
                yield "[\"" + DataFakerCustomUtils.generateSelection() + "\"]";

            case SpecialAttributes.LNK_DAYS_PER_WEEK:
                yield "[\"SEL_" + convertNumberToWord(daysPerWeek).toUpperCase() + "\"]";

            case SpecialAttributes.LNK_INTERNSHIP_DURATION:
                yield "[SEL_DURATION_12_WEEKS]";

            default:
                yield null;
        };
    }

    private String convertNumberToWord(int num) {
        if (num > 10)
            throw new IllegalArgumentException(
                    "This function cannot handle number above 10 or less than 0.");
        List<String> numbers = new ArrayList<>(Arrays.asList(
                "Zero", "One", "Two", "Three", "Four", "Five",
                "Six", "Seven", "Eight", "Nine", "Ten"));
        return numbers.get(num);
    }
}
