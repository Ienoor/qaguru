package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.assertj.core.api.AbstractStringAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.ARRAY_2D;

public class SelenideFilesTest {

    ClassLoader cl = SelenideFilesTest.class.getClassLoader();

    @BeforeEach
    void setUp() {
        Configuration.browserSize = "1920x1080";
//        Configuration.browser = "firefox";

    }

    @Test
    void selenideFilesDownload() throws Exception {
        open("https://github.com/junit-team/junit5/blob/main/README.md");
        File downloadedFile = $("#raw-url").download();

        try (InputStream is = new FileInputStream(downloadedFile)) {
            byte[] bytes = is.readAllBytes();
            String textContent = new String(bytes, StandardCharsets.UTF_8);
            assertThat(textContent).contains("This repository is the home of _JUnit 5_.");

        }

    }

    @Test
    void selenideUploadFile() {
        open("https://fineuploader.com/demos.html");
        $("input[type=file]").uploadFromClasspath("qa-2-min.png");
        $(".qq-file-info").shouldHave(Condition.text("qa-2-min.png"));
        System.out.println();
    }

    @Test
    void pdfParseTest() throws Exception {
        open("https://junit.org/junit5/docs/current/user-guide/");
        File pdf_download = $("a[href=\"junit-user-guide-5.9.3.pdf\"]").download();
        PDF pdfContent = new PDF(pdf_download);
        assertThat(pdfContent.author).contains("Sam Brannen");
    }

    @Test
    void xlsParseTest() throws Exception {
        try (InputStream resourceAsStream = cl.getResourceAsStream("file_example_XLS_10.xls")) {
            assert resourceAsStream != null;
            XLS xls = new XLS(resourceAsStream);
            assertThat(xls.excel.getSheetAt(0).getRow(1).getCell(1).getStringCellValue()).contains("Dulce");
        }
    }

    @Test
    void csvParseTest() throws Exception {
        try (
                InputStream resourceAsStream = cl.getResourceAsStream("cities.csv");
                CSVReader reader = new CSVReader(new InputStreamReader(resourceAsStream))
        ){
            List<String[]> content = reader.readAll();
            System.out.println();
            assertThat(content.get(1)[8]).contains("Youngstown");
        }
    }
}
