package life.genny.datagenerator.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import life.genny.datagenerator.BaseTestCase;
import life.genny.qwandaq.utils.testsuite.JUnitTester;

public class FileUtilsTest extends BaseTestCase {

    @Test
    void generateFileInputStream() {
        // TODO: Need to update the test validation for generating file
        new JUnitTester<FileInputStream, Boolean>()
                .setTest((input) -> {
                    try {
                        return Expected(input.input.readAllBytes() != null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return Expected(false);
                })

                .createTest("Generate FileInputStream Check 1")
                .setInput(FileUtils.generateFileInputStream())
                .setExpected(true)
                .build()

                .createTest("Generate FileInputStream Check 2")
                .setInput(FileUtils.generateFileInputStream("sample-text-1"))
                .setExpected(true)
                .build()

                .assertAll();

    }

    @Test
    void generateFile() {
        new JUnitTester<File, Boolean>()
                .setTest((input) -> {
                    return Expected(input.input != null);
                })

                .createTest("Generate File Check 1")
                .setInput(FileUtils.generateFile())
                .setExpected(true)
                .build()

                .createTest("Generate File Check 2")
                .setInput(FileUtils.generateFile("sample-text-2"))
                .setExpected(true)
                .build()

                .assertAll();
    }
}
