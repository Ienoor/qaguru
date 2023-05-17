package guru.qa.homework;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import guru.qa.SelenideFilesTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckingFilesInArchiveTest {

    ClassLoader cl = SelenideFilesTest.class.getClassLoader();

    String[][] files = {{"cities.csv"}, {"file_example_XLS_10.xls"}, {"junit-user-guide-5.9.3.pdf"}};

    @ParameterizedTest
    @CsvSource({"cities.csv", "file_example_XLS_10.xls", "junit-user-guide-5.9.3.pdf"})
    void checkingFilesInArchiveTest(String fileName) throws Exception {
        try (
                InputStream resourceAsStream = cl.getResourceAsStream("hw.zip");
                ZipInputStream zis = new ZipInputStream(resourceAsStream)

        ) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {

                switch (fileName) {
                    case entry.getName() -> {
                        CSVReader reader = new CSVReader(new InputStreamReader(zis));
                        List<String[]> content = reader.readAll();
                        assertThat(content.get(1)[8]).contains("Youngstown");

                    }
                    case fileName -> {
                        PDF pdfContent = new PDF(zis);
                        assertThat(pdfContent.author).contains("Sam Brannen");
                    }
                    case ".xls" -> {
                        XLS xls = new XLS(zis);
                        assertThat(xls.excel.getSheetAt(0).getRow(1).getCell(1).getStringCellValue()).contains("Dulce");
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + entry.getName());
                }


            }
        }


    }
}


