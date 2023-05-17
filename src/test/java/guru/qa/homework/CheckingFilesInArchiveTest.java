package guru.qa.homework;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import guru.qa.SelenideFilesTest;
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

    @ParameterizedTest
    @CsvSource({".csv", ".xls", ".pdf"})
    void checkingFilesInArchiveTest(String extendsFile) throws Exception {
        try (
                InputStream resourceAsStream = cl.getResourceAsStream("hw.zip");
                ZipInputStream zis = new ZipInputStream(resourceAsStream)

        ) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(extendsFile)) {
                    CSVReader reader = new CSVReader(new InputStreamReader(zis));
                    List<String[]> content = reader.readAll();
                    assertThat(content.get(1)[8]).contains("Youngstown");
                }
                if (entry.getName().endsWith(extendsFile)) {
                    PDF pdfContent = new PDF(zis);
                    assertThat(pdfContent.author).contains("Sam Brannen");
                }
                if (entry.getName().endsWith(extendsFile)) {
                    CSVReader reader = new CSVReader(new InputStreamReader(zis));
                    XLS xls = new XLS(zis);
                    assertThat(xls.excel.getSheetAt(0).getRow(1).getCell(1).getStringCellValue()).contains("Dulce");
                }

            }
        }
    }
}


